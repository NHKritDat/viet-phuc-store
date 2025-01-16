package com.nextrad.vietphucstore.dtos.requests.api.viettel;

public record GetServicesRequest(
        int provinceId,
        int districtId,
        double weight,
        double price,
        double moneyCollection
) {
}
