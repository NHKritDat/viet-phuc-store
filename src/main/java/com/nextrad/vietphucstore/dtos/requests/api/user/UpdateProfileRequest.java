package com.nextrad.vietphucstore.dtos.requests.api.user;

import com.nextrad.vietphucstore.enums.user.UserGender;

import java.util.Date;

public record UpdateProfileRequest(
        String name,
        Date dob,
        UserGender gender,
        String province,
        String district,
        String address,
        String phone
) {
}
