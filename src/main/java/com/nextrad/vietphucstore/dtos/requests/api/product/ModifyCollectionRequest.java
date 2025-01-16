package com.nextrad.vietphucstore.dtos.requests.api.product;

import java.util.List;

public record ModifyCollectionRequest(
        String name,
        String description,
        List<String> images
) {
}
