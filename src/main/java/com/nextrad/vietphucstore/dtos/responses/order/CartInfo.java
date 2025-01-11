package com.nextrad.vietphucstore.dtos.responses.order;

import java.util.UUID;

public record CartInfo(
        UUID productQuantityId,
        String name,
        String image,
        double unitPrice,
        int weight,
        String size,
        int quantity,
        int maxQuantity
) {
}
