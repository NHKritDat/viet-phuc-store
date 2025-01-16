package com.nextrad.vietphucstore.dtos.responses.api.viettel;

public record DistrictResponse(
        int DISTRICT_ID,
        int DISTRICT_VALUE,
        String DISTRICT_NAME,
        int PROVINCE_ID
) {
}
