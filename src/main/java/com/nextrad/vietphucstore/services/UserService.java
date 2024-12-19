package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.user.LoginPassword;
import com.nextrad.vietphucstore.dtos.requests.user.LogoutRequest;
import com.nextrad.vietphucstore.dtos.requests.user.RegisterRequest;
import com.nextrad.vietphucstore.dtos.requests.user.TokenRequest;
import com.nextrad.vietphucstore.dtos.responses.user.TokenResponse;

import java.util.UUID;

public interface UserService {
    boolean existsByIdAndStatus(UUID id, boolean status);
    TokenResponse login(LoginPassword request);
    TokenResponse login(TokenRequest request);
    TokenResponse getAccessToken(TokenRequest request);
    String logout(LogoutRequest request);
    String register(RegisterRequest request);
}
