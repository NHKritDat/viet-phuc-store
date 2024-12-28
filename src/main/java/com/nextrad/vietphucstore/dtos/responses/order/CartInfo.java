package com.nextrad.vietphucstore.dtos.responses.order;

import java.util.UUID;

public record CartInfo(
        UUID productQuantityId,
        String name,
        double unitPrice,
        String size,
        int quantity
) {
}