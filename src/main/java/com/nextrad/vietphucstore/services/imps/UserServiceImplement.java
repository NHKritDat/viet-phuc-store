package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.requests.user.*;
import com.nextrad.vietphucstore.dtos.responses.user.TokenResponse;
import com.nextrad.vietphucstore.entities.user.Token;
import com.nextrad.vietphucstore.entities.user.User;
import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.enums.user.UserRole;
import com.nextrad.vietphucstore.enums.user.UserStatus;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nextrad.vietphucstore.repositories.user.TokenRepository;
import com.nextrad.vietphucstore.repositories.user.UserRepository;
import com.nextrad.vietphucstore.services.UserService;
import com.nextrad.vietphucstore.utils.EmailUtil;
import com.nextrad.vietphucstore.utils.PageableUtil;
import com.nextrad.vietphucstore.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PageableUtil pageableUtil;
    private final TokenUtil tokenUtil;
    private final EmailUtil emailUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean existsByIdAndStatus(UUID id, boolean status) {
        return tokenRepository.existsByIdAndAvailable(id, status);
    }

    @Override
    public TokenResponse login(LoginPassword request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        String refreshToken = tokenUtil.genRefreshToken(user);
        tokenRepository.save(tokenUtil.createEntity(refreshToken, true));
        return new TokenResponse(tokenUtil.genAccessToken(user), refreshToken);
    }

    @Override
    public TokenResponse login(AuthRequest request) {
        String[] info = tokenUtil.getInfo(request.auth());
        Optional<User> user = userRepository.findByEmail(info[0]);
        if (user.isPresent()) {
            String refreshToken = tokenUtil.genRefreshToken(user.get());
            tokenRepository.save(tokenUtil.createEntity(refreshToken, true));
            return new TokenResponse(tokenUtil.genAccessToken(user.get()), refreshToken);
        } else {
            User newUser = new User();
            newUser.setEmail(info[0]);
            newUser.setFullName(info[1]);
            newUser.setAvatar(info[2]);
            newUser.setStatus(UserStatus.VERIFIED);
            newUser.setCreatedBy(info[0]);
            newUser.setUpdatedBy(info[0]);
            newUser = userRepository.save(newUser);

            String refreshToken = tokenUtil.genRefreshToken(newUser);
            tokenRepository.save(tokenUtil.createEntity(refreshToken, true));
            return new TokenResponse(tokenUtil.genAccessToken(newUser), refreshToken);
        }
    }

    @Override
    public TokenResponse getAccessToken(AuthRequest request) {
        String[] tokenId = tokenUtil.getJwtInfo(request.auth());
        Token token = tokenRepository.findById(UUID.fromString(tokenId[0]))
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));
        if (!token.isAvailable())
            throw new AppException(ErrorCode.UNAVAILABLE_TOKEN);
        token.setAvailable(false);
        tokenRepository.save(token);

        User user = new User();
        user.setId(UUID.fromString(tokenId[1]));
        user.setFullName(tokenId[2]);
        user.setEmail(tokenId[3]);
        user.setAddress(tokenId[4]);
        user.setPhone(tokenId[5]);
        user.setAvatar(tokenId[6]);
        user.setRole(UserRole.valueOf(tokenId[7]));
        user.setStatus(UserStatus.valueOf(tokenId[8]));
        user.setCreatedBy(tokenId[9]);
        user.setCreatedDate(new Date(Long.parseLong(tokenId[10])));
        user.setUpdatedBy(tokenId[11]);
        user.setUpdatedDate(new Date(Long.parseLong(tokenId[12])));

        String refreshToken = tokenUtil.genRefreshToken(user);
        tokenRepository.save(tokenUtil.createEntity(refreshToken, true));
        return new TokenResponse(tokenUtil.genAccessToken(user), refreshToken);
    }

    @Override
    public String logout(LogoutRequest request) {
        Token refreshToken = tokenRepository.findById(UUID.fromString(tokenUtil.getJwtId(request.refreshToken())))
                .orElseThrow(() -> new AppException(ErrorCode.TOKEN_NOT_FOUND));
        refreshToken.setAvailable(false);
        tokenRepository.save(refreshToken);
        tokenRepository.save(tokenUtil.createEntity(request.accessToken(), false));
        return "Logout success.";
    }

    @Override
    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        if (!request.password().equals(request.confirmPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setAddress(request.address());
        user.setPhone(request.phone());
        user.setAvatar(request.avatar());
        user.setCreatedBy(request.email());
        user.setUpdatedBy(request.email());
        userRepository.save(user);

        emailUtil.verifyEmail(request.email(), request.fullName(), tokenUtil.genAccessToken(user));
        return "Please check your email to verify your account.";
    }

    @Override
    public String verifyEmail(String token) {
        String[] info = tokenUtil.getJwtInfo(token);
        User user = userRepository.findByEmail(info[3])
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(UserStatus.VERIFIED);
        userRepository.save(user);
        tokenRepository.save(tokenUtil.createEntity(token, false));
        return "Your email has been verified! Please login to continue.";
    }

    @Override
    public String forgotPassword(AuthRequest request) {
        User user = userRepository.findByEmail(request.auth())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        emailUtil.resetPassword(user.getEmail(), user.getFullName(), tokenUtil.genAccessToken(user));
        return "Please check your email to reset your password.";
    }

    @Override
    public String resetPassword(ChangePasswordRequest request) {
        String[] info = tokenUtil.getJwtInfo(request.auth());
        User user = userRepository.findByEmail(info[3])
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!request.newPassword().equals(request.confirmPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        tokenRepository.save(tokenUtil.createEntity(request.auth(), false));
        return "Your password has been reset successfully! Please login to continue.";
    }

    @Override
    public String changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.auth(), user.getPassword()))
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        if (!request.newPassword().equals(request.confirmPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        return "Your password has been changed successfully!";
    }
}
