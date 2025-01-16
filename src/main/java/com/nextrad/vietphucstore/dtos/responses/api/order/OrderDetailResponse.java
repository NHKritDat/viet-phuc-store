package com.nextrad.vietphucstore.dtos.responses.api.order;

import java.util.List;

public record OrderDetailResponse(
        String productName,
        List<String> productImage,
        String size,
        int quantity,
        double unitPrice
) {
}
