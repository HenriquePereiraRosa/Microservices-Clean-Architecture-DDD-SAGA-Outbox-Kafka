package com.h.udemy.java.uservices.order.service.domain.exception.messages;

import com.h.udemy.java.uservices.order.service.domain.ApiEnvTestConfig;
import com.h.udemy.java.uservices.order.service.domain.messages.I18n;
import org.junit.jupiter.api.*;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class I18nTest extends ApiEnvTestConfig {

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