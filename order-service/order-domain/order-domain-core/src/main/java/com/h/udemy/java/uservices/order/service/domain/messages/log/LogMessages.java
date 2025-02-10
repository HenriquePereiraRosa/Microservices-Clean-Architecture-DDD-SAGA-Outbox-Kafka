package com.h.udemy.java.uservices.order.service.domain.messages.log;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;

import static java.text.MessageFormat.format;


@Getter
@Slf4j
public enum LogMessages {


    APP_NAME_DESCRIPTION("app.name.description"), //todo: do refactor
    ORDER_ID_CREATED_SUCCESSFULLY("order.id.created.successfully"), //todo: do refactor
    ORDER_ID_CREATING("order.id.creating"), //todo: do refactor

    ORDER_TRACKING_ALL("order.id.all-track-order"), //todo: do refactor
    ORDER_TRACKING_BY_TRACKING_ID("order.id.track-order-by-tracking-id"); //todo: do refactor

    final static String FILE_PATH = "messages_order_domain.log.log-messages";
    final String key;
    LogMessages(String key) {
        this.key = key;
    }

    public String get() { //todo: remove when all string are removed from property files
        ResourceBundle bundle = ResourceBundle
                .getBundle(FILE_PATH,
                        LocaleContextHolder.getLocale());
        return bundle.getString(key);
    }

    public String build(Object... objects) {
        return format(key, objects);
    }
}
