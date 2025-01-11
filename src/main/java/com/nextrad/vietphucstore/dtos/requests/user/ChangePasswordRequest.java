package com.nextrad.vietphucstore.dtos.requests.user;

public record ChangePasswordRequest(
        String auth, //old password or token
        String newPassword,
        String confirmPassword
) {
}
