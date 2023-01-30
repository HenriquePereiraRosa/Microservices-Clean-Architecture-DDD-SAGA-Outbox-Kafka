package com.h.udemy.java.uservices.order.service.domain.exception.messages;

import com.h.udemy.java.uservices.order.service.domain.ApiEnvTestConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static com.h.udemy.java.uservices.domain.messages.Messages.ORDER_CREATED_SUCCESSFULLY;
import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.APP_NAME_DESCRIPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class I18nTest extends ApiEnvTestConfig {

    @Test
    @Order(1)
    void getAppName_EN() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        assertEquals("Order Service Domain Core", APP_NAME_DESCRIPTION.get());
    }
    @Test
    @Order(1)
    void getMsg_EN() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        assertEquals("Order created successfully", ORDER_CREATED_SUCCESSFULLY.get());
    }

    @Test
    @Order(2)
    void getMsg_DE() {
        LocaleContextHolder.setLocale(Locale.GERMAN);
        assertEquals("Bestellung erfolgreich erstellt", ORDER_CREATED_SUCCESSFULLY.get());
    }

    @Test
    @Order(3)
    void getMsg_FR() {
        LocaleContextHolder.setLocale(Locale.FRENCH);
        assertEquals("Commande créée avec succès", ORDER_CREATED_SUCCESSFULLY.get());
    }

    @AfterAll
    void setLocateToEn() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }
}