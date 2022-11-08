package com.h.udemy.java.uservices.order.service.domain.messages;

import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;


@Getter
public enum I18n {

    APP_NAME_DESCRIPTION("app.name.description"),

    ORDER_CREATED_SUCCESSFULLY("order.created.successfully"),
    ORDER_ID_INITIATED("order.id.initiated"),
    ORDER_ID_PAYMENT_CANCELLING("order.id.payment-cancelling"),
    ORDER_ID_PAYMENT_CANCELLED("order.id.payment-cancelled"),
    ORDER_ID_CREATED("order.id.created"),
    ORDER_TRACKING_ID_NOT_FOUND("err.order.tracking-id.not-found"),

    ERR_ORDER_NOT_CORRECT_INIT_STATE("err.order.not-correct-state-initialization"),
    ERR_ORDER_PRICE_MUST_GREATER_ZERO("err.order.price-must-greater-zero"),
    ERR_ORDER_TOTAL_AND_ORDER_PRICES_DIFF("err.order.total-and-order-prices-are-diff"),
    ERR_ORDER_ITEM_PRICE_INVALID("err.order.item-price-is-invalid"),
    ERR_ORDER_NOT_CORRECT_STATE_F_PAY("err.order.not-correct-state-for-pay"),
    ERR_ORDER_NOT_CORRECT_STATE_F_APPROVE("err.order.not-correct-state-for-approve"),
    ERR_ORDER_NOT_CORRECT_STATE_F_INIT_CANCEL("err.order.not-correct-state-for-init-cancel"),
    ERR_ORDER_NOT_CORRECT_STATE_F_CANCEL("err.order.not-correct-state-for-cancel"),
    ERR_ORDER_COULD_NOT_BE_SAVED("err.order.could-not-be-saved"),
    ERR_RESTAURANT_ID_NOT_ACTIVE("err.restaurant.id.not-active"),
    ERR_RESTAURANT_NOT_FOUND("err.restaurant.not-found"),
    ERR_CUSTOMER_NOT_FOUND("err.customer.not-found");

    String key;
    I18n(String key) {
        this.key = key;
    }

    public String getMsg() {
        ResourceBundle bundle = ResourceBundle
                .getBundle("messages.messages", LocaleContextHolder.getLocale());
        return bundle.getString(key);
    }
}
