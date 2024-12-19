package com.nextrad.vietphucstore.dtos.requests.user;

public record UpdateProfileRequest(
        String fullName,
        String address,
        String phone,
        String avatar
) {
}
