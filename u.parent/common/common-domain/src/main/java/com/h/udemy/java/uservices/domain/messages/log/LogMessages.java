package com.h.udemy.java.uservices.domain.messages.log;

import lombok.Getter;

import static java.text.MessageFormat.format;

@Getter
public enum LogMessages {

    ORDER_RECEIVED_ID("Received OrderCreatedEvent. orderId: {0}"),
    ORDER_CANCEL_RECEIVED_ID("Received OrderCancelEvent. orderId: {0}"),
    ORDER_SUCCESSFUL_RESPONSE_KAFKA("PaymentRequestAvroModel sent to Kafka for order id: {0}"),
    ORDER_SENT_REQUEST_KAFKA("Received successful response from Kafka for order id: {0}, Topic: {1}, Partition: {2}, " +
            "Offset: {3}, Timestamp {4}"),
    ORDER_ID_PROCESSED_SUCCESS("Order {0} processed successfully"),
    ORDER_ID_PROCESSED_FAILED("Order {0} failed!"),

    ORDER_KAFKA_NUMBER_MODEL_RESPONSES_RECEIVED("{0} number of {1} responses received. Keys: {2}, Partitions: {3}, " +
            "Offsets: {4}"),

    ORDER_ERROR_WHILE_SENDING_REQUEST_AVRO("Error while sending {0} message {1} to topic {2}"),
    ORDER_ERROR_MSG_SENDING_REQ_AVRO_KAFKA("Error while sending {0} message to Kafka. orderId {1}, error: {2}"),

    PAYMENT_REQUEST_PROCESSING_FOR_ID("Processing payment for order id: {0}"),
    PAYMENT_REQUEST_CANCELLING_FOR_ID("Cancelling payment for order id: {0}"),
    PAYMENT_REQUEST_SUCCESS_FOR_ID("Payment request success for order id: {0}!"),
    PAYMENT_REQUEST_CANCELED_FOR_ID("Payment request canceled for order id: {0}!"),
    PAYMENT_X_REQUESTS_RECEIVED("{0} number of payment request received with keys: {1}, partitions: {2}, and offsets: {4}"),
    PAYMENT_ERR_NOT_ENOUGH_CREDIT("CustomerId: {0}, does not have enough credit for payment!"),
    PAYMENT_ERR_CREDIT_HISTORY_NOT_EQUALS("Credit history total and Current credit is not equal for CustomerId: {0}!"),
    PAYMENT_ERR_FAILED_FOR_ORDER_ID("Payment request failed for order id: {0}!"),
    PAYMENT_ERR_STATUS_UNSUPPORTED("Unsupported payment order status: {0}"),

    EVENT_RECEIVED("Received {0} for {1} id: {2}"),
    EVENT_SENT_TO_KAFKA("{0} sent to KAFKA for {1} id: {2}"),
    EVENT_ERR_SENT_TO_KAFKA("Error while sending {0} message to KAFKA with {1} id: {2}, error: {3}");

    final String key;

    LogMessages(final String key) {
        this.key = key;
    }

    public String build(Object... objects) {
        return format(key, objects);
    }
}
