package com.nextrad.vietphucstore.dtos.requests.api.user;

public record LoginGoogle(
        String email,
        String name,
        String avatar
) {
}
