package com.nextrad.vietphucstore.dtos.responses.user;

import com.nextrad.vietphucstore.entities.user.Token;

public record CheckTokenResult(
        boolean valid,
        Token token
) {
}
