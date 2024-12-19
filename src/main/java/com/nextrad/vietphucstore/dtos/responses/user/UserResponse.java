package com.nextrad.vietphucstore.dtos.responses.user;

import com.nextrad.vietphucstore.enums.user.UserRole;
import com.nextrad.vietphucstore.enums.user.UserStatus;

import java.util.Date;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        String address,
        String phone,
        String avatar,
        UserRole role,
        UserStatus status,
        String createdBy,
        Date createdDate,
        String updatedBy,
        Date updatedDate
) {
}
