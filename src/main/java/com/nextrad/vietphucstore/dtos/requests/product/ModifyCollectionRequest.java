package com.nextrad.vietphucstore.dtos.requests.product;

import java.util.List;

public record ModifyCollectionRequest(
        String name,
        String description,
        List<String> images
) {
}
