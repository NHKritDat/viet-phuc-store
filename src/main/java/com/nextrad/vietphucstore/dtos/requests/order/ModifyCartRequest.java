package com.nextrad.vietphucstore.dtos.requests.order;

import java.util.UUID;

public record ModifyCartRequest(
        UUID productQuantityId,
        int quantity
) {
}
