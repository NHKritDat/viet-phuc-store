package com.nextrad.vietphucstore.dtos.responses.product;

import com.nextrad.vietphucstore.enums.product.ProductStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ProductDetail(
        UUID id,
        String name,
        String description,
        double unitPrice,
        List<String> pictures,
        ProductStatus status,
        String collectionName,
        String typeName,
        Map<String, Integer> sizeQuantities
) {
}
