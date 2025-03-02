package com.nextrad.vietphucstore.dtos.responses.api.product;

import java.util.UUID;

public record SearchProductForAI(
        UUID id,
        String name,
        double unitPrice,
        String description,
        double rating
) {
}
