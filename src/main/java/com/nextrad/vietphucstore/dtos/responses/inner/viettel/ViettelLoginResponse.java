package com.nextrad.vietphucstore.dtos.responses.inner.viettel;

import java.util.Date;

public record ViettelLoginResponse(
        int userId,
        String token,
        int partner,
        String phone,
        Date expired,
        int source,
        boolean infoUpdated
) {
}
