package com.nextrad.vietphucstore.dtos.requests.product;

import java.util.UUID;

public record TopProductRequest(
        UUID id,
        String name,
        double unitPrice,
        String pictures,
        long count
) {
}
