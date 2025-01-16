package com.nextrad.vietphucstore.dtos.requests.api.order;

import java.util.UUID;

public record ModifyCartRequest(
        UUID productQuantityId,
        int quantity
) {
}
