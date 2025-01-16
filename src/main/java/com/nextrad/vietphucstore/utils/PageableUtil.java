package com.nextrad.vietphucstore.utils;

import com.nextrad.vietphucstore.dtos.requests.inner.pageable.PageableRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PageableUtil {

    public <T> Pageable getPageable(Class<T> t, PageableRequest request) {
        return PageRequest.of(request.page(), request.size(), Sort.by(request.direction(),
                validProperties(t, request.properties())));
    }

    private <T> String[] validProperties(Class<T> t, String... properties) {
        Set<String> fieldNames = Arrays.stream(t.getDeclaredFields()).map(Field::getName).collect(Collectors.toSet());
        String[] validProperties = Arrays.stream(properties).filter(fieldNames::contains).toArray(String[]::new);
        return validProperties.length > 0 ? validProperties : new String[]{"id"};
    }
}
