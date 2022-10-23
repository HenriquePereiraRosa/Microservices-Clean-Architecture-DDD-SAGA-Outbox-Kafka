package com.h.udemy.java.uservices.order.service.domain.exception.msg;

import com.h.udemy.java.uservices.order.service.domain.entity.ApiEnvTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class I18nTest extends ApiEnvTest {

    @Test
    @Order(1)
    void getMsg_EN() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        assertEquals("Order Service Domain Core", I18n.APP_NAME_DESCRIPTION.getMsg());
    }

    @Test
    @Order(2)
    void getMsg_DE() {
        LocaleContextHolder.setLocale(Locale.GERMAN);
        assertEquals("Bestellen Sie Service Domain Core", I18n.APP_NAME_DESCRIPTION.getMsg());
    }

    @Test
    @Order(3)
    void getMsg_FR() {
        LocaleContextHolder.setLocale(Locale.FRENCH);
        assertEquals("Cœur de domaine de service de commande", I18n.APP_NAME_DESCRIPTION.getMsg());
    }

    @AfterAll
    void setLocateToEn() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }
}