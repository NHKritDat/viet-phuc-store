package com.nextrad.vietphucstore.dtos.responses.api.user;

import com.nextrad.vietphucstore.enums.user.UserGender;
import com.nextrad.vietphucstore.enums.user.UserRole;
import com.nextrad.vietphucstore.enums.user.UserStatus;

import java.util.Date;
import java.util.UUID;

public record UserDetail(
        UUID id,
        String name,
        Date dob,
        UserGender gender,
        String email,
        String province,
        String district,
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
