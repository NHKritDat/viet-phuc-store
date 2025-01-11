package com.nextrad.vietphucstore.dtos.responses.user;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
