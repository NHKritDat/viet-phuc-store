package com.nextrad.vietphucstore.dtos.responses.product;

import java.util.UUID;

public record TopProduct(
        UUID id,
        String name,
        double unitPrice,
        String picture,
        double rating,
        int count
) {
}
