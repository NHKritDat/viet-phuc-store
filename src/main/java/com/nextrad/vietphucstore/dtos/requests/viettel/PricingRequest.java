package com.nextrad.vietphucstore.dtos.requests.viettel;

public record PricingRequest(
        double weight,
        double price,
        double moneyCollection,
        String orderService,
        int receiverProvince,
        int receiverDistrict
) {
}
