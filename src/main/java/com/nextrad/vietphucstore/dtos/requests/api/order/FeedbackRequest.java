package com.nextrad.vietphucstore.dtos.requests.api.order;

public record FeedbackRequest(
        String content,
        int rating
) {
}
