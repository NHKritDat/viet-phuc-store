package com.nextrad.vietphucstore.dtos.responses.viettel;

import java.util.List;

public record ViettelServicesResponse(
        String MA_DV_CHINH,
        String TEN_DICHVU,
        int GIA_CUOC,
        String THOI_GIAN,
        int EXCHANGE_WEIGHT,
        List<ViettelExtraServiceResponse> EXTRA_SERVICE
) {
}
