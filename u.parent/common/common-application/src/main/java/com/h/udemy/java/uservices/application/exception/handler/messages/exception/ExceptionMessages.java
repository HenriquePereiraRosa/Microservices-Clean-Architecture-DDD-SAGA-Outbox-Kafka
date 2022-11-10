package com.h.udemy.java.uservices.application.exception.handler.messages.exception;

import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;


@Getter
public enum ExceptionMessages {

    UNEXPECTED_ERROR("unexpected-error"),
    BAD_REQUEST("bad-request");

    String key;
    ExceptionMessages(String key) {
        this.key = key;
    }

    public String get() {
        ResourceBundle bundle = ResourceBundle
                .getBundle("messages.exception-messages", LocaleContextHolder.getLocale());
        return bundle.getString(key);
    }
}
