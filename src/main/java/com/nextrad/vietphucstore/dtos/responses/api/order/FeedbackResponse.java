package com.nextrad.vietphucstore.dtos.responses.api.order;

import java.util.Date;
import java.util.UUID;

public record FeedbackResponse(
        UUID id,
        String productName,
        String productImage,
        String content,
        int rating,
        String username,
        Date createdAt,
        UUID productId
) {
}
