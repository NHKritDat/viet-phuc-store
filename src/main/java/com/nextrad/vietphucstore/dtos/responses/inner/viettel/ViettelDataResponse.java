package com.nextrad.vietphucstore.dtos.responses.inner.viettel;

public record ViettelDataResponse<T>(
        int status,
        boolean error,
        String message,
        T data
) {
}
