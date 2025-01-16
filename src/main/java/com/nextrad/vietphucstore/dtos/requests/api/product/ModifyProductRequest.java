package com.nextrad.vietphucstore.dtos.requests.api.product;

import com.nextrad.vietphucstore.enums.product.ProductStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ModifyProductRequest(
        String name,
        String description,
        double unitPrice,
        List<String> pictures,
        int weight,
        ProductStatus status,
        UUID collectionId,
        UUID typeId,
        Map<UUID, Integer> sizeQuantities
) {
}
