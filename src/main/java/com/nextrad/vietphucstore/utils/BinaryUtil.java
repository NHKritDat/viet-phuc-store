package com.nextrad.vietphucstore.utils;

import org.springframework.stereotype.Component;

@Component
public class BinaryUtil {
    public int checkBinary(String[] sizes, String[] types, String[] collections) {
        int size = sizes.length == 0 ? 0 : 100;
        int type = types.length == 0 ? 0 : 10;
        int collection = collections.length == 0 ? 0 : 1;
        return size + type + collection;
    }
}
