package com.nextrad.vietphucstore.dtos.requests.order;

public record FeedbackRequest(
        String content,
        int rating
) {
}
