package com.nextrad.vietphucstore.dtos.responses.order;

import com.nextrad.vietphucstore.enums.order.OrderStatus;
import com.nextrad.vietphucstore.enums.order.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record OrderResponse(
        String id,
        String email,
        String name,
        String address,
        String phone,
        double productTotal,
        double shippingFee,
        String shippingMethod,
        PaymentMethod paymentMethod,
        OrderStatus status,
        Map<UUID, OrderDetailResponse> details
) {
}
