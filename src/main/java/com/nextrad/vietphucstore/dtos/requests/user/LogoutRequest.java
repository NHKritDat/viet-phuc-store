package com.nextrad.vietphucstore.dtos.requests.user;

public record LogoutRequest(
        String accessToken,
        String refreshToken
) {
}
