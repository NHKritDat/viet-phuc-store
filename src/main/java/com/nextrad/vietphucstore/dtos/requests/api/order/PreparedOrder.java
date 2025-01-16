package com.nextrad.vietphucstore.dtos.requests.api.order;

import com.nextrad.vietphucstore.enums.order.PaymentMethod;

import java.util.List;

public record PreparedOrder(
        String email,
        String name,
        String province,
        String district,
        String address,
        String phone,
        double shippingFee,
        String shippingMethod,
        PaymentMethod paymentMethod,
        List<SelectedProductRequest> details
) {
}
