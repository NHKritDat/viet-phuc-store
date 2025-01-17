package com.nextrad.vietphucstore.dtos.responses.api.product;

import java.util.Date;
import java.util.UUID;

public record ProductFeedback(
        UUID feedbackId,
        String content,
        int rating,
        String username,
        String avatar,
        Date createdAt
) {
}
