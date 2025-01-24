package com.h.udemy.java.uservices.order.service.domain.messages.log;

import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;

import static java.text.MessageFormat.format;


@Getter
public enum LogMessages {


    APP_NAME_DESCRIPTION("app.name.description"), //todo: do refactor
    ORDER_ID_CREATED_SUCCESSFULLY("order.id.created.successfully"), //todo: do refactor
    ORDER_ID_CREATING("order.id.creating"), //todo: do refactor

    ORDER_TRACKING_ALL("order.id.all-track-order"), //todo: do refactor
    ORDER_TRACKING_BY_TRACKING_ID("order.id.track-order-by-tracking-id"), //todo: do refactor

    ORDER_RECEIVED("Received {0} OrderPaymentOutboxMessage with ids: [ {1} ], sending to message bus!"),

    OUTBOX_MESSAGE_COULDNT_BE_SAVED("{0} with ID [{1}], could not be saved!"),
    OUTBOX_MESSAGE_SAVED("{0} saved with ID: [{1}]"),
    ORDER_ID_STATUS_UPDATED("{0} with ID [{1}] updated with STATUS : {2}"),

    ORDER_MESSAGES_RECEIVED_FOR_CLEANUP("Received {0} {1} for clean-up. Payloads: {2}"),
    ORDER_MESSAGES_DELETED("{0} {1} deleted.");

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
