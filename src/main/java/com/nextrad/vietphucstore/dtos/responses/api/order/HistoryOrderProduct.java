package com.nextrad.vietphucstore.dtos.responses.api.order;

import com.nextrad.vietphucstore.enums.order.PaymentMethod;

import java.util.UUID;

public record HistoryOrderProduct(
        String orderId,
        UUID orderDetailId,
        String image,
        String name,
        PaymentMethod paymentMethod,
        String size,
        int quantity,
        double unitPrice,
        double shippingFee,
        boolean feedback,
        UUID productId
) {
}
