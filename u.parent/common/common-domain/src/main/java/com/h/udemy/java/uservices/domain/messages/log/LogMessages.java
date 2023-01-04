package com.h.udemy.java.uservices.domain.messages.log;

import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;


@Getter
public enum LogMessages {

    ORDER_RECEIVED_ID("order.id.received"),
    ORDER_CANCEL_RECEIVED_ID("order.id.cancel-received"),
    ORDER_SUCCESSFUL_RESPONSE_KAFKA("order.id.successful-response-kafka"),
    ORDER_SENT_REQUEST_KAFKA("order.id.msg.payment-request-avro-kafka-sent"),
    ORDER_ID_PROCESSED_SUCCESS("order.id.processed.success"),
    ORDER_ID_PROCESSED_FAILED("order.id.processed.failed"),

    ORDER_KAFKA_NUMBER_MODEL_RESPONSES_RECEIVED("order.kafka.number-model-responses-received"),

    ORDER_ERROR_WHILE_SENDING_REQUEST_AVRO("order.error.while-sending-request-avro-model"),
    ORDER_ERROR_MSG_SENDING_REQ_AVRO_KAFKA("order.error.msg.sending.request-avro-model-kafka"),

    PAYMENT_REQUEST_SUCCESS_FOR_ID("payment.success.for-order-id"),
    PAYMENT_REQUEST_CANCELED_FOR_ID("payment.canceled.for-order-id"),
    PAYMENT_ERR_NOT_ENOUGH_CREDIT("payment.error.not-enough-credit"),
    PAYMENT_ERR_CREDIT_HISTORY_NOT_EQUALS("payment.error.credit-history-not-equals"),
    PAYMENT_ERR_FAILED_FOR_ORDER_ID("payment.error.failed-for-order-id");

    final String key;
    LogMessages(final String key) {
        this.key = key;
    }

    public String get() {
        final ResourceBundle bundle = ResourceBundle
                .getBundle("messages_common_domain.log.log-messages",
                        LocaleContextHolder.getLocale());
        return bundle.getString(key);
    }
}
