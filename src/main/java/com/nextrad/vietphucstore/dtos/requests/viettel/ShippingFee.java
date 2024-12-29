package com.nextrad.vietphucstore.dtos.requests.viettel;

public record ShippingFee(
        int weight,
        int price,
        int moneyCollection,
        String serviceAdd,
        int receiverProvince,
        int receiverDistrict
) {
}
