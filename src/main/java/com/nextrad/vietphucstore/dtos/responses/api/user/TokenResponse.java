package com.nextrad.vietphucstore.dtos.responses.api.user;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
