package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.api.user.*;
import com.nextrad.vietphucstore.dtos.requests.inner.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.responses.inner.user.LoginResponse;
import com.nextrad.vietphucstore.dtos.responses.api.user.SearchUser;
import com.nextrad.vietphucstore.dtos.responses.api.user.TokenResponse;
import com.nextrad.vietphucstore.dtos.responses.api.user.UserDetail;
import com.nextrad.vietphucstore.enums.user.UserRole;
import com.nextrad.vietphucstore.enums.user.UserStatus;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {
    boolean existsByIdAndStatus(UUID id, boolean status);

    LoginResponse login(LoginPassword request);

    TokenResponse login(LoginGoogle request);

    TokenResponse getAccessToken(AuthRequest request);

    String logout(LogoutRequest request);

    String register(RegisterRequest request);

    String verifyEmail(String token);

    String forgotPassword(AuthRequest request);

    String resetPassword(ChangePasswordRequest request);

    String changePassword(ChangePasswordRequest request);

    Page<SearchUser> getUsers(String search, PageableRequest request);

    UserDetail getUser(UUID id);

    UserDetail getCurrentUser();

    UserDetail createUser(UserModifyRequest request);

    UserDetail updateUser(UUID id, UserModifyRequest request);

    String deleteUser(UUID id);

    UserDetail updateProfile(UpdateProfileRequest request);

    UserDetail updateAvatar(UpdateAvatarRequest request);

    boolean isEmailExist(String email);

    void createDefaultUser(String email, UserRole role, UserStatus status);

    void deleteUnverifiedUsers(long time);
}
