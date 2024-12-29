package com.nextrad.vietphucstore.dtos.requests.viettel;

public record ShippingFeeRequest(
        int PRODUCT_WEIGHT,
        int PRODUCT_PRICE,
        int MONEY_COLLECTION,
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
