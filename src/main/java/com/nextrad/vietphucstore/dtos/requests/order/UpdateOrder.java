package com.nextrad.vietphucstore.dtos.requests.order;

import com.nextrad.vietphucstore.enums.order.OrderStatus;

public record UpdateOrder(
    String email,
    String name,
    String phone,
    OrderStatus status
) {
}
