package com.nextrad.vietphucstore.dtos.responses.viettel;

public record ViettelPricingResponse(
        int MONEY_TOTAL_OLD,
        int MONEY_TOTAL,
        int MONEY_TOTAL_FEE,
        int MONEY_FEE,
        int MONEY_COLLECTION_FEE,
        int MONEY_OTHER_FEE,
        int MONEY_VAS,
        int MONEY_VAT,
        int KPI_HT
) {
}
