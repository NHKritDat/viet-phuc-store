package com.nextrad.vietphucstore.dtos.requests.viettel;

public record ViettelGetServicesRequest(
        int SENDER_PROVINCE,
        int SENDER_DISTRICT,
        int RECEIVER_PROVINCE,
        int RECEIVER_DISTRICT,
        String PRODUCT_TYPE,
        double PRODUCT_WEIGHT,
        double PRODUCT_PRICE,
        double MONEY_COLLECTION,
        int TYPE
) {
}
