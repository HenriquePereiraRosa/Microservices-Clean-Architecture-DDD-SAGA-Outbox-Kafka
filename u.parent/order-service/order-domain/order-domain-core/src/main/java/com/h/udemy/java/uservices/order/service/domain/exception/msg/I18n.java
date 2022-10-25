package com.h.udemy.java.uservices.order.service.domain.exception.msg;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;


@Getter
public enum I18n { //        implements MessageSourceAware {
    APP_NAME_DESCRIPTION("app.name.description"),

    ORDER_ID_INITIATED("order.id.initiated"),
    ORDER_ID_PAYMENT_CANCELLING("order.id.payment-cancelling"),
    ORDER_ID_PAYMENT_CANCELLED("order.id.payment-cancelled"),

    ERR_ORDER_NOT_CORRECT_INIT_STATE("err.order.not-correct-state-initialization"),
    ERR_ORDER_PRICE_MUST_GREATER_ZERO("err.order.price-must-greater-zero"),
    ERR_ORDER_TOTAL_AND_ORDER_PRICES_DIFF("err.order.total-and-order-prices-are-diff"),
    ERR_ORDER_ITEM_PRICE_INVALID("err.order.item-price-is-invalid"),
    ERR_ORDER_NOT_CORRECT_STATE_F_PAY("err.order.not-correct-state-for-pay"),
    ERR_ORDER_NOT_CORRECT_STATE_F_APPROVE("err.order.not-correct-state-for-approve"),
    ERR_ORDER_NOT_CORRECT_STATE_F_INIT_CANCEL("err.order.not-correct-state-for-init-cancel"),
    ERR_ORDER_NOT_CORRECT_STATE_F_CANCEL("err.order.not-correct-state-for-cancel"),
    ERR_RESTAURANT_ID_NOT_ACTIVE("err.restaurant.id.not-active");

    private static MessageSource messageSource;

    String key;
    I18n(String key) {
        this.key = key;
    }

    public static void setMessageSource(Object messageSource) {
        I18n.messageSource = (MessageSource) messageSource;
    }

    public String getMsg() {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}
