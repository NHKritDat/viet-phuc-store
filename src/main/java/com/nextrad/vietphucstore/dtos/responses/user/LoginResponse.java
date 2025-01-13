package com.nextrad.vietphucstore.dtos.responses.user;

public record LoginResponse(
        TokenResponse response,
        String message
) {
}
