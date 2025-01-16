package com.nextrad.vietphucstore.dtos.requests.api.user;

public record LogoutRequest(
        String accessToken,
        String refreshToken
) {
}
