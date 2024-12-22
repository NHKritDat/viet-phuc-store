package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.user.*;
import com.nextrad.vietphucstore.dtos.responses.user.TokenResponse;
import com.nextrad.vietphucstore.dtos.responses.user.UserResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {
    boolean existsByIdAndStatus(UUID id, boolean status);

    TokenResponse login(LoginPassword request);

    TokenResponse login(AuthRequest request);

    TokenResponse getAccessToken(AuthRequest request);

    String logout(LogoutRequest request);

    String register(RegisterRequest request);

    String verifyEmail(String token);

    String forgotPassword(AuthRequest request);

    String resetPassword(ChangePasswordRequest request);

    String changePassword(ChangePasswordRequest request);

    Page<UserResponse> getUsers(String search, PageableRequest request);

    UserResponse getUser(UUID id);

    UserResponse getCurrentUser();

    UserResponse createUser(UserModifyRequest request);

    UserResponse updateUser(UUID id, UserModifyRequest request);

    String deleteUser(UUID id);

    UserResponse updateProfile(UpdateProfileRequest request);
}
