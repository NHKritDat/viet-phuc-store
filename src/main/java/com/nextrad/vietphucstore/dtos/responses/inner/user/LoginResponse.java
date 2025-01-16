package com.nextrad.vietphucstore.dtos.responses.inner.user;

import com.nextrad.vietphucstore.dtos.responses.api.user.TokenResponse;

public record LoginResponse(
        TokenResponse response,
        String message
) {
}
