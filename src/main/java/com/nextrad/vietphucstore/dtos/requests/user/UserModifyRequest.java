package com.nextrad.vietphucstore.dtos.requests.user;

import com.nextrad.vietphucstore.enums.user.UserRole;
import com.nextrad.vietphucstore.enums.user.UserStatus;

public record UserModifyRequest(
        String fullName,
        String email,
        String password,
        String address,
        String phone,
        String avatar,
        UserRole role,
        UserStatus status
) {
}
