package com.nextrad.vietphucstore.dtos.responses.api.order;

import com.nextrad.vietphucstore.enums.order.OrderStatus;
import com.nextrad.vietphucstore.enums.order.PaymentMethod;

public record CurrentOrderHistory(
        String orderId,
        String name,
        String address,
        PaymentMethod paymentMethod,
        OrderStatus orderStatus,
        double total
) {
}
