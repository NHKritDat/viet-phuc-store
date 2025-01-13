package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.user.*;
import com.nextrad.vietphucstore.dtos.responses.user.LoginResponse;
import com.nextrad.vietphucstore.dtos.responses.user.SearchUser;
import com.nextrad.vietphucstore.dtos.responses.user.TokenResponse;
import com.nextrad.vietphucstore.dtos.responses.user.UserDetail;
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
}
