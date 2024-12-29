package com.nextrad.vietphucstore.dtos.requests.user;

import com.nextrad.vietphucstore.enums.user.UserGender;
import com.nextrad.vietphucstore.enums.user.UserRole;
import com.nextrad.vietphucstore.enums.user.UserStatus;

import java.util.Date;

public record UserModifyRequest(
        String name,
        Date dob,
        UserGender gender,
        String email,
        String password,
        String province,
        String district,
        String address,
        String phone,
        String avatar,
        UserRole role,
        UserStatus status
) {
}
