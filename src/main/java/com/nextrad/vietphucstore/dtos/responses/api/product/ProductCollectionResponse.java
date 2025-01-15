package com.nextrad.vietphucstore.dtos.responses.api.product;

import java.util.List;
import java.util.UUID;

public record ProductCollectionResponse(
        UUID id,
        String name,
        String description,
        List<String> images,
        boolean deleted
) {
}
