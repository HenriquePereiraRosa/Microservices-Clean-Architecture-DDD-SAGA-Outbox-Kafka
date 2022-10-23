package com.h.udemy.java.uservices.order.service.domain.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Valid {

    static final String UUID_PATTERN = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})";

    public static boolean isUUID(Object target) {
        Pattern pattern = Pattern.compile(UUID_PATTERN);
        Matcher matcher = pattern.matcher(target.toString());
        return matcher.matches();
    }
}
