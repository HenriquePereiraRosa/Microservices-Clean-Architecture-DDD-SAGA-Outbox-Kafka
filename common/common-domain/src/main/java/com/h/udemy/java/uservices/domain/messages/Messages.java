package com.h.udemy.java.uservices.domain.messages;

import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;

import static java.text.MessageFormat.format;


@Getter
@Deprecated
public enum Messages {
    PUBLISHING_EVENT_FOR_ID("event.publishing.with-id"),


    ORDER_CREATED_SUCCESSFULLY("order.created.successfully"),
    ORDER_ID_INITIATED("order.id.initiated"),
    ORDER_ID_PAYMENT_CANCELLING("order.id.payment-cancelling"),
    ORDER_ID_PAYMENT_CANCELLED("order.id.payment-cancelled"),
    ORDER_ID_CREATED("order.id.created"),
    ORDER_ROLLBACK_DONE_MSGS("order.id.rollback.done"),

    ERR_ORDER_TRACKING_ID_NOT_FOUND("err.order.tracking-id.not-found"),
    ERR_ORDER_NOT_CORRECT_INIT_STATE("err.order.not-correct-state-initialization"),
    ERR_ORDER_PRICE_MUST_GREATER_ZERO("err.order.price-must-greater-zero"),
    ERR_ORDER_TOTAL_AND_ORDER_PRICES_DIFF("err.order.total-and-order-prices-are-diff"),
    ERR_ORDER_ITEM_PRICE_INVALID("err.order.item-price-is-invalid"),
    ERR_ORDER_NOT_CORRECT_STATE_F_PAY("err.order.not-correct-state-for-pay"),
    ERR_ORDER_NOT_CORRECT_STATE_F_APPROVE("err.order.not-correct-state-for-approve"),
    ERR_ORDER_NOT_CORRECT_STATE_F_INIT_CANCEL("err.order.not-correct-state-for-init-cancel"),
    ERR_ORDER_NOT_CORRECT_STATE_F_CANCEL("err.order.not-correct-state-for-cancel"),
    ERR_ORDER_COULD_NOT_BE_PUBLISHED("err.order.could-not-be-published"),
    ERR_ORDER_COULD_NOT_BE_SAVED("err.order.could-not-be-saved"),
    ERR_ORDER_NOT_FOUND("Order NOT FOUND! id: {0}"),


    ERR_RESTAURANT_ID_NOT_ACTIVE("err.restaurant.id.not-active"),
    ERR_RESTAURANT_NOT_FOUND("err.restaurant.not-found"),
    ERR_RESTAURANT_NONE_FOUND("err.restaurant.no-one-found"),
    ERR_CUSTOMER_NOT_FOUND("err.customer.not-found"),


    PAYMENT_SUCCESSFULLY_RECEIVED_COMPLETE_EVENT_FOR_ID("payment.success.received-payment-complete-event"),
    PAYMENT_SUCCESSFULLY_RECEIVED_CANCEL_EVENT_FOR_ID("payment.success.received-payment-cancel-event"),
    PAYMENT_PUB_EVENT_PAYMENT_AND_ORDER_FOR_ID("payment.event.publishing.pub-for-payment-and-order-w-id"),

    ERR_PAYMENT_TOTAL_PRICE_MUST_BE_GRATER_THAN_ZERO("err.payment.total-price-must-be-greater-than-zero"),
    ERR_PAYMENT_COULD_NOT_FIND_CREDIT_ENTRY_FOR_CUSTOMER_ID("err.payment.could-not-find-credit-entry-for-customer-id"),
    ERR_PAYMENT_COULD_NOT_FIND_CREDIT_HISTORY_FOR_CUSTOMER_ID("err.payment.could-not-find-credit-history-for-customer-id"),
    ERR_PAYMENT_NOT_ENOUGH_CREDIT("err.payment.not-enough-credit"),
    ERR_PAYMENT_CREDIT_HISTORY_NOT_EQUALS("err.payment.credit-history-not-equals"),
    ERR_PAYMENT_COULD_NOT_BE_FOUND_WITH_ORDER_ID("err.payment.order-could-not-be-found-with-id"),


    RESTAURANT_COULD_NOT_BE_FOUND_WITH_ORDER_ID("err.payment.order-could-not-be-found-with-id");

    final String key;
    Messages(final String key) {
        this.key = key;
    }

    public String get() {
        final ResourceBundle bundle = ResourceBundle
                .getBundle("messages_common_domain.messages", LocaleContextHolder.getLocale());
        return bundle.getString(key);
    }

    public String build(Object... objects) {
        return format(key, objects);
    }
}
