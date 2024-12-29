package com.nextrad.vietphucstore.dtos.requests.pageable;

import org.springframework.data.domain.Sort;

public record PageableRequest(
        int page,
        int size,
        Sort.Direction direction,
        String... properties
) {
}
