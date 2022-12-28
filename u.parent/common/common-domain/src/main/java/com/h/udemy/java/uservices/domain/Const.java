package com.h.udemy.java.uservices.domain;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Const {

    private Const(){} // prevent instance creation
    public static final ZoneId ZONED_UTC = ZoneId.of("UTC");

    public static final ZonedDateTime UTC_DATE_TIME_NOW = ZonedDateTime.now(ZONED_UTC);
}
