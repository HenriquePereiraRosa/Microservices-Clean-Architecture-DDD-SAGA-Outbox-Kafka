package com.h.udemy.java.uservices.domain;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Constants {

    private Constants(){}

    public static final String BASE_PACKAGE = "com.h.udemy.java.uservices";
    public static final ZoneId ZONED_UTC = ZoneId.of("UTC");

    public static ZonedDateTime getZonedDateTimeNow() {
        return ZonedDateTime.now(ZONED_UTC);
    }
}
