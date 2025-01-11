package com.nextrad.vietphucstore.dtos.requests.product;

import com.nextrad.vietphucstore.entities.product.ProductQuantity;

import java.util.UUID;

public record SelectedProductRequest(
        UUID productQuantityId,
        int quantity
) {
}
