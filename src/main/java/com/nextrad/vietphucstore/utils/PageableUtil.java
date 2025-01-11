package com.nextrad.vietphucstore.utils;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PageableUtil {

    public <T> Pageable getPageable(Class<T> t, PageableRequest request) {
        return PageRequest.of(request.page(), request.size(), Sort.by(request.direction(),
                validProperties(t, request.properties())));
    }

    private <T> String[] validProperties(Class<T> t, String... properties) {
        List<String> validProperties = new ArrayList<>();
        //Check field contains in class
        for (String property : properties) {
            if (Arrays.stream(t.getDeclaredFields()).anyMatch(field -> field.getName().equals(property)))
                validProperties.add(property);
        }
        return validProperties.toArray(String[]::new);
    }
}
