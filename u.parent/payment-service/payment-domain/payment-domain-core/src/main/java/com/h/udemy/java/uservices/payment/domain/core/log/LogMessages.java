package com.h.udemy.java.uservices.payment.domain.core.log;

import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;


@Getter
public enum LogMessages {


    APP_NAME_DESCRIPTION("app.name.description");

    final static String FILE_PATH = "messages_payment_domain.log.log-messages";
    final String key;
    LogMessages(String key) {
        this.key = key;
    }

    public String get() {
        ResourceBundle bundle = ResourceBundle
                .getBundle(FILE_PATH,
                        LocaleContextHolder.getLocale());
        return bundle.getString(key);
    }
}
