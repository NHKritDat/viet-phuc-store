package com.nextrad.vietphucstore.dtos.requests.product;

import java.util.UUID;

public record SelectedProductRequest(
        UUID productQuantityId,
        int quantity
) {
}
