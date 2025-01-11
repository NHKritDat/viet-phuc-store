package com.nextrad.vietphucstore.utils;

import org.springframework.stereotype.Component;

@Component
public class RegexUtil {
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$";

    public boolean invalidPassword(String password) {
        return !password.matches(PASSWORD_REGEX);
    }
}
