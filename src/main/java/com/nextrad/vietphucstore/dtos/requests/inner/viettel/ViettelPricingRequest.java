package com.nextrad.vietphucstore.dtos.requests.inner.viettel;

public record ViettelPricingRequest(
        double PRODUCT_WEIGHT,
        double PRODUCT_PRICE,
        double MONEY_COLLECTION,
        String ORDER_SERVICE_ADD,
        String ORDER_SERVICE,
        int SENDER_PROVINCE,
        int SENDER_DISTRICT,
        int RECEIVER_PROVINCE,
        int RECEIVER_DISTRICT,
        String PRODUCT_TYPE,
        int NATIONAL_TYPE
) {
}
