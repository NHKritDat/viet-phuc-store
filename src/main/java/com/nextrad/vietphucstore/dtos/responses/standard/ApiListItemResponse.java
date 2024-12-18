package com.nextrad.vietphucstore.dtos.responses.standard;

import java.util.List;

public record ApiListItemResponse<T>(
        List<T> data,
        int size,
        int page,
        long totalSize,
        int totalPage,
        String message
) {
}
