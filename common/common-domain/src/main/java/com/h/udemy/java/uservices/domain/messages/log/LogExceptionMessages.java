package com.h.udemy.java.uservices.domain.messages.log;

import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;


@Getter
public enum LogExceptionMessages {

    UNEXPECTED_ERROR("unexpected-error"),
    BAD_REQUEST("bad-request");

    final String key;
    LogExceptionMessages(final String key) {
        this.key = key;
    }

    public String get() {
        final ResourceBundle bundle = ResourceBundle
                .getBundle("messages_common_domain.error.exception-messages",
                        LocaleContextHolder.getLocale());
        return bundle.getString(key);
    }
}
