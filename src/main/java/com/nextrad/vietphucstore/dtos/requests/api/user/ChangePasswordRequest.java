package com.nextrad.vietphucstore.dtos.requests.api.user;

public record ChangePasswordRequest(
        String auth, //old password or token
        String newPassword,
        String confirmPassword
) {
}
