package com.nextrad.vietphucstore.dtos.responses.order;

import com.nextrad.vietphucstore.enums.order.PaymentMethod;

import java.util.Date;

public record TransactionsResponse(
        PaymentMethod paymentMethod,
        boolean paymentStatus,
        Date paymentDate,
        String orderId,
        Date orderCreatedDate,
        double shippingFee,
        double totalTransaction
) {
}
