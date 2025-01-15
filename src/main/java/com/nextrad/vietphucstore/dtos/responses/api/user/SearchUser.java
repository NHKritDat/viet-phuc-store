package com.nextrad.vietphucstore.dtos.responses.api.user;

import com.nextrad.vietphucstore.enums.user.UserStatus;

import java.util.UUID;

public record SearchUser(
        UUID id,
        String name,
        String email,
        String avatar,
        UserStatus status
) {
}
