package com.nextrad.vietphucstore.services.impls;

import com.nextrad.vietphucstore.dtos.requests.api.user.*;
import com.nextrad.vietphucstore.dtos.requests.inner.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.responses.api.user.SearchUser;
import com.nextrad.vietphucstore.dtos.responses.api.user.TokenResponse;
import com.nextrad.vietphucstore.dtos.responses.api.user.UserDetail;
import com.nextrad.vietphucstore.dtos.responses.inner.user.CheckTokenResult;
import com.nextrad.vietphucstore.dtos.responses.inner.user.LoginResponse;
import com.nextrad.vietphucstore.entities.user.Token;
import com.nextrad.vietphucstore.entities.user.User;
import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.enums.user.UserRole;
import com.nextrad.vietphucstore.enums.user.UserStatus;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nextrad.vietphucstore.repositories.user.TokenRepository;
import com.nextrad.vietphucstore.repositories.user.UserRepository;
import com.nextrad.vietphucstore.services.UserService;
import com.nextrad.vietphucstore.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapperUtil objectMapperUtil;
    private final PageableUtil pageableUtil;
    private final TokenUtil tokenUtil;
    private final EmailUtil emailUtil;
    private final RegexUtil regexUtil;

    @Override
    public boolean existsByIdAndStatus(UUID id, boolean status) {
        return tokenRepository.existsByIdAndAvailable(id, status);
    }

    @Override
    public LoginResponse login(LoginPassword request) {
        User user = userRepository.findByEmailAndStatus((request.email()), UserStatus.VERIFIED)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        String message = user.getRole() != UserRole.STAFF ?
                "Bạn đã đăng nhập thành công" :
                "Bạn đã đăng nhập thành công trang cho Staff";
        String refreshToken = tokenUtil.genRefreshToken(user);
        CompletableFuture.runAsync(() -> tokenRepository.save(tokenUtil.createEntity(refreshToken, true)));
        return new LoginResponse(
                new TokenResponse(tokenUtil.genAccessToken(user), refreshToken),
                message
        );
    }

    @Override
    public TokenResponse login(LoginGoogle request) {
        Optional<User> user = userRepository.findByEmailAndStatus(request.email(), UserStatus.VERIFIED);
        if (user.isPresent()) {
            String refreshToken = tokenUtil.genRefreshToken(user.get());
            CompletableFuture.runAsync(() -> tokenRepository.save(tokenUtil.createEntity(refreshToken, true)));
            return new TokenResponse(tokenUtil.genAccessToken(user.get()), refreshToken);
        } else {
            User newUser = userRepository.save(objectMapperUtil.mapUser(request, new User()));
            String refreshToken = tokenUtil.genRefreshToken(newUser);
            CompletableFuture.runAsync(() -> tokenRepository.save(tokenUtil.createEntity(refreshToken, true)));
            return new TokenResponse(tokenUtil.genAccessToken(newUser), refreshToken);
        }
    }

    @Override
    public TokenResponse getAccessToken(AuthRequest request) {
        String[] jwtInfo = tokenUtil.getJwtInfo(request.auth());
        Token token = tokenRepository.findById(UUID.fromString(jwtInfo[0]))
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));
        if (!token.isAvailable())
            throw new AppException(ErrorCode.UNAVAILABLE_TOKEN);

        CompletableFuture.runAsync(() -> {
            token.setAvailable(false);
            tokenRepository.save(token);
        });

        User user = objectMapperUtil.mapUser(jwtInfo, new User());
        String refreshToken = tokenUtil.genRefreshToken(user);
        CompletableFuture.runAsync(() -> tokenRepository.save(tokenUtil.createEntity(refreshToken, true)));
        return new TokenResponse(tokenUtil.genAccessToken(user), refreshToken);
    }

    @Override
    public String logout(LogoutRequest request) {
        Token refreshToken = tokenRepository.findById(UUID.fromString(tokenUtil.getJwtId(request.refreshToken())))
                .orElseThrow(() -> new AppException(ErrorCode.TOKEN_NOT_FOUND));
        CompletableFuture.runAsync(() -> {
            refreshToken.setAvailable(false);
            tokenRepository.save(refreshToken);
        });
        CompletableFuture.runAsync(() -> {
            CheckTokenResult result = tokenUtil.checkToken(request.accessToken(), false);
            if (result.valid())
                tokenRepository.save(result.token());
        });
        return "Đăng xuất thành công!";
    }

    @Override
    public String register(RegisterRequest request) {
        if (userRepository.existsByEmailAndStatusNotLike(request.email(), UserStatus.DELETED))
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        if (!request.password().equals(request.confirmPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        if (regexUtil.invalidPassword(request.password()))
            throw new AppException(ErrorCode.PASSWORD_NOT_STRONG);

        User user = userRepository.save(objectMapperUtil.mapUser(request, new User()));

        emailUtil.verifyEmail(request.email(), request.name(), tokenUtil.genAccessToken(user));
        return "Vui lòng kiểm tra email để xác thực tài khoản.";
    }

    @Override
    public String verifyEmail(String token) {
        String email = tokenUtil.getEmailFromJwt(token);
        User user = userRepository.findByEmailAndStatus(email, UserStatus.UNVERIFIED)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(UserStatus.VERIFIED);
        userRepository.save(user);
        CompletableFuture.runAsync(() -> {
            CheckTokenResult result = tokenUtil.checkToken(token, false);
            if (result.valid())
                tokenRepository.save(result.token());
        });
        return "Email của bạn đã được xác thực thành công! Vui lòng đăng nhập lại để tiếp tục.";
    }

    @Override
    public String forgotPassword(AuthRequest request) {
        User user = userRepository.findByEmailAndStatus(request.auth(), UserStatus.VERIFIED)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        emailUtil.resetPassword(user.getEmail(), user.getName(), tokenUtil.genAccessToken(user));
        return "Vui lòng kiểm tra email để đặt lại mật khẩu.";
    }

    @Override
    public String resetPassword(ChangePasswordRequest request) {
        String email = tokenUtil.getEmailFromJwt(request.auth());
        User user = userRepository.findByEmailAndStatus(email, UserStatus.VERIFIED)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!request.newPassword().equals(request.confirmPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        if (regexUtil.invalidPassword(request.newPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_STRONG);
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        CompletableFuture.runAsync(() -> {
            CheckTokenResult result = tokenUtil.checkToken(request.auth(), false);
            if (result.valid())
                tokenRepository.save(result.token());
        });
        return "Mật khẩu của bạn đã được đặt lại thành công! Vui lòng đăng nhập lại để tiếp tục.";
    }

    @Override
    public String changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.auth(), user.getPassword()))
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        if (!request.newPassword().equals(request.confirmPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        if (regexUtil.invalidPassword(request.newPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_STRONG);
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        return "Mật khẩu của bạn đã được thay đổi thành công!";
    }

    @Override
    public Page<SearchUser> getUsers(String search, PageableRequest request) {
        return userRepository
                .findByRoleNotLikeAndNameContainsIgnoreCase(
                        UserRole.STAFF, search, pageableUtil.getPageable(User.class, request)
                )
                .map(objectMapperUtil::mapSearchUser);
    }

    @Override
    public UserDetail getUser(UUID id) {
        return objectMapperUtil.mapUserDetail(
                userRepository.findByIdAndRoleNotLike(id, UserRole.STAFF)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND))
        );
    }

    @Override
    public UserDetail getCurrentUser() {
        return objectMapperUtil.mapUserDetail(
                userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND))
        );
    }

    @Override
    public UserDetail createUser(UserModifyRequest request) {
        return objectMapperUtil.mapUserDetail(userRepository.save(
                objectMapperUtil.mapUser(request, new User())
        ));
    }

    @Override
    public UserDetail updateUser(UUID id, UserModifyRequest request) {
        return objectMapperUtil.mapUserDetail(userRepository.save(
                objectMapperUtil.mapUser(request, userRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)))
        ));
    }

    @Override
    public String deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
        return "Người dùng này đã bị xóa!";
    }

    @Override
    public UserDetail updateProfile(UpdateProfileRequest request) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!request.name().isBlank())
            user.setName(request.name());
        if (request.dob() != null)
            user.setDob(request.dob());
        if (request.gender() != null)
            user.setGender(request.gender());
        if (!request.province().isBlank())
            user.setProvince(request.province());
        if (!request.district().isBlank())
            user.setDistrict(request.district());
        if (!request.address().isBlank())
            user.setAddress(request.address());
        if (!request.phone().isBlank())
            user.setPhone(request.phone());
        return objectMapperUtil.mapUserDetail(userRepository.save(user));
    }

    @Override
    public UserDetail updateAvatar(UpdateAvatarRequest request) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setAvatar(request.avatar());
        return objectMapperUtil.mapUserDetail(userRepository.save(user));
    }

    @Override
    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void createDefaultUser(String email, UserRole role, UserStatus status) {
        User user = new User();
        user.setName(email);
        user.setEmail(email);
        user.setRole(role);
        user.setStatus(status);
        user.setCreatedBy(email);
        user.setUpdatedBy(email);
        userRepository.save(user);
    }

    @Override
    public void deleteUnverifiedUsers(long time) {
        userRepository.deleteByStatusAndUpdatedDateAfter(UserStatus.UNVERIFIED, new Date(System.currentTimeMillis() + time));
    }

}
