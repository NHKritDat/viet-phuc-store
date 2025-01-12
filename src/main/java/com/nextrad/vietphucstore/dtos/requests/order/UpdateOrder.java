package com.nextrad.vietphucstore.dtos.requests.order;

public record UpdateOrder(
        String email,
        String name,
        String phone
) {
}
