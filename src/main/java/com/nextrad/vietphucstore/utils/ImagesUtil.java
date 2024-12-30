package com.nextrad.vietphucstore.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ImagesUtil {

    public List<String> convertStringToImages(String images) {
        return Arrays.asList(images.substring(1, images.length() - 1).split(", "));
    }
}
