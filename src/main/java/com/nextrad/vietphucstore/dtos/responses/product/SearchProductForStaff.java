package com.nextrad.vietphucstore.dtos.responses.product;

import com.nextrad.vietphucstore.enums.product.ProductStatus;

import java.util.UUID;

public record SearchProductForStaff(
        UUID id,
        String name,
        double unitPrice,
        String picture,
        double rating,
        ProductStatus status
) {
}
