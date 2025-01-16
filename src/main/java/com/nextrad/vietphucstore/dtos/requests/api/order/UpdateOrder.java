package com.nextrad.vietphucstore.dtos.requests.api.order;

public record UpdateOrder(
        String email,
        String name,
        String phone
) {
}
