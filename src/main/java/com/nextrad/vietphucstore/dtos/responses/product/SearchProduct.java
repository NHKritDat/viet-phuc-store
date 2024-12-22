package com.nextrad.vietphucstore.dtos.responses.product;

import java.util.UUID;

public record SearchProduct(
        UUID id,
        String name,
        double unitPrice
) {
}
