package com.nextrad.vietphucstore.dtos.requests.api.order;

import java.util.UUID;

public record SelectedProductRequest(
        UUID productQuantityId,
        int quantity
) {
}
