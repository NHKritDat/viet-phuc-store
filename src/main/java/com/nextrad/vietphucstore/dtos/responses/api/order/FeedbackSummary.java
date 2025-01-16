package com.nextrad.vietphucstore.dtos.responses.api.order;

public record FeedbackSummary(
        long total5Star,
        long total4Star,
        long total3Star,
        long total2Star,
        long total1Star,
        long totalReviews
) {
}
