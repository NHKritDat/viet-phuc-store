package com.nextrad.vietphucstore.dtos.requests.api.user;

public record RegisterRequest(
        String name,
        String email,
        String password,
        String confirmPassword
) {
}
