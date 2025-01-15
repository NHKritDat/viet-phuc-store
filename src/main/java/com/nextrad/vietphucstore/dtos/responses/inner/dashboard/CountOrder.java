package com.nextrad.vietphucstore.dtos.responses.inner.dashboard;

public record CountOrder(
        long pending,
        long pickup,
        long delivery,
        long transit,
        long delivered,
        long canceled
) {
}
