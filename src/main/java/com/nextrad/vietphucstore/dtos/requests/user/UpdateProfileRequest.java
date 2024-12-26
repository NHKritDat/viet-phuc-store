package com.nextrad.vietphucstore.dtos.requests.user;

import com.nextrad.vietphucstore.enums.user.UserGender;

import java.util.Date;

public record UpdateProfileRequest(
        String name,
        Date dob,
        UserGender gender,
        String address,
        String phone
) {
}
