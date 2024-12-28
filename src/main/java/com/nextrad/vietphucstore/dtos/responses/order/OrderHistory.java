package com.nextrad.vietphucstore.dtos.responses.order;

import com.nextrad.vietphucstore.enums.order.PaymentMethod;

import java.util.UUID;

public record OrderHistory(
        String orderId,
        UUID orderDetailId,
        String image,
        String name,
        PaymentMethod paymentMethod,
        String size,
        int quantity,
        double unitPrice
) {
}
