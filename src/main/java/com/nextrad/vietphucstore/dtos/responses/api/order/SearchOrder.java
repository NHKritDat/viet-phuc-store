package com.nextrad.vietphucstore.dtos.responses.api.order;

import com.nextrad.vietphucstore.enums.order.OrderStatus;
import com.nextrad.vietphucstore.enums.order.PaymentMethod;

public record SearchOrder(
        String id,
        String email,
        double productTotal,
        double shippingFee,
        String shippingMethod,
        PaymentMethod paymentMethod,
        OrderStatus status
) {
}
