package com.nextrad.vietphucstore.dtos.responses.inner.order;

import com.nextrad.vietphucstore.entities.product.ProductQuantity;

public record SelectedProduct(
        ProductQuantity productQuantity,
        int quantity
) {
}
