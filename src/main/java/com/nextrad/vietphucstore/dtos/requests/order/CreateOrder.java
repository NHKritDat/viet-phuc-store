package com.nextrad.vietphucstore.dtos.requests.order;

import com.nextrad.vietphucstore.enums.order.PaymentMethod;

public record CreateOrder(
        String email,
        String name,
        String province,
        String district,
        String address,
        String phone,
        double shippingFee,
        PaymentMethod paymentMethod
) {
}
