package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.user.*;
import com.nextrad.vietphucstore.dtos.responses.user.TokenResponse;

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
}
