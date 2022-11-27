package com.h.udemy.java.uservices.order.service.domain.messages.log;

import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;


@Getter
public enum LogMessages {

    ORDER_ID_CREATED_SUCCESSFULLY("order.id.created.successfully"),
    ORDER_ID_CREATING("order.id.creating"),

    ORDER_TRACKING_ALL("order.id.all-track-order"),
    ORDER_TRACKING_BY_TRACKING_ID("order.id.track-order-by-tracking-id");

    String key;
    LogMessages(String key) {
        this.key = key;
    }

    public String get() {
        ResourceBundle bundle = ResourceBundle
                .getBundle("messages.log.log-messages", LocaleContextHolder.getLocale());
        return bundle.getString(key);
    }
}
