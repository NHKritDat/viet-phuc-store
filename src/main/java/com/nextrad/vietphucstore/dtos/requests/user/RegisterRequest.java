package com.nextrad.vietphucstore.dtos.requests.user;

public record RegisterRequest(
        String fullName,
        String email,
        String password,
        String confirmPassword,
        String address,
        String phone,
        String avatar
) {
}
