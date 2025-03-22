package com.h.udemy.java.uservices.domain.messages.log;

import lombok.Getter;

import static java.text.MessageFormat.format;

@Getter
public enum LogMessages {

    // COMMON
    KAFKA_X_REQUESTS_RECEIVED("{0} of {1} requests received with keys: {2}, partitions: {3}, offsets: {4}"),
    KAFKA_PROCESSING_FOR_ID("Processing {0} for order id: {1}"),

    PUB_PROCESSING_APPROVAL_FOR_ID("Processing [{0}] approval for order id: [{1}]"),

    EVENT_RECEIVED("Received {0} for {1} id: [{2}]"),
    EVENT_SENT_TO_KAFKA("{0} sent to KAFKA. OrderId [{1}], SagaId [{2}]"),
    EVENT_X_ID_SENT_TO_KAFKA("{0} sent to KAFKA. {1} id [{2}]"),
    EVENT_SENT_TO_KAFKA_AT("{0} sent to KAFKA at: {1}"),
    EVENT_ERR_SENT_TO_KAFKA("Error while sending {0} message to KAFKA with {1}. Order id [{2}] and Saga id [{3}], error: {4}"),
    EVENT_ERR_OPTIMISTIC_LOCK("Caught optimistic locking exception in {0} for order id: {1}"),
    ERR_UNIQUE_VIOLATION_IN_REQUEST_LISTENER("Caught unique constraint exception with sql state: {0}  in {1} for order id: {2}"),
    ERR_RETHROWN_DATA_ACCESS_EXCEPTION("Throwing DataAccessException in {0}"),

    PROCESS_OPERATION_COMPLETED("{0} operation process with ID: {1}, is complete."),
    PROCESS_ROLLBACK_OPERATION_COMPLETED("{0} rollback operation with ID: {1}, is complete. failures messages {2}"),
    OUTBOX_MESSAGE_ALREADY_PROCESSED("{0} with SAGA ID: {1}, already processed."),
    OUTBOX_MESSAGE_SAGA_ID_ALREADY_ROLLED_BACK("An Outbox Message with SAGA ID: {0}, already Rolled back!"),
    OUTBOX_MESSAGE_COULD_NOT_BE_FOUND("{0} with SAGA Status: {1}, could NOT be found!"),
    OUTBOX_MESSAGE_COULD_NOT_BE_READ("{0} could not be mapped!"),
    OUTBOX_MESSAGE_RECEIVED("Received {0}. Order id [{1}], Saga id [{2}]."),
    OUTBOX_MESSAGES_RECEIVED_SENDING_TO_KAFKA("Received {0} {1}s with ids: [ {2} ], sending to kafka!"),
    OUTBOX_MESSAGE_SAVED("{0} saved with ID: [{1}]"),
    OUTBOX_MESSAGE_UPDATED("Order outbox table status is updated as: {0}"),
    OUTBOX_MESSAGE_ALREADY_SAVED("An outbox message with saga id [{0}] is already saved to database!"),
    OUTBOX_MESSAGES_SENT_TO_MSG_BUS("{0} {1} sent to message bus!"),
    OUTBOX_OBJ_COULD_NOT_BE_FOUND("{0} outbox object could NOT be found for type {1}!"),

    ERR_OUTBOX_MESSAGE_COULD_NOT_BE_SAVED("{0} with ID [{1}], could not be saved!"),
    ERR_ORDER_COULD_NOT_BE_MAPPED("{0} with ID {1}, could not be serialized to JSON."),

    ERR_NOT_FOUND("{0} id [{1}] not found!"),

    // COMMON MODULES
    ID_CREATED_ORDER_RESPONSE("Returning {0}  for {1} with ID: [{1}]"),
    SAVED_SUCCESSFULLY("{0} saved successfully!"),

    // ORDER
    ORDER_RECEIVED_ID("Received OrderCreatedEvent. orderId: {0}"),
    ORDER_CANCEL_RECEIVED_ID("Received OrderCancelEvent. orderId: {0}"),
    ORDER_SUCCESSFUL_RESPONSE_KAFKA("PaymentRequestAvroModel sent to Kafka for order id: {0}"),
    ORDER_SENT_REQUEST_KAFKA("Received successful response from Kafka for order id: {0}," +
            " Topic: {1}, Partition: {2}, Offset: {3}, Timestamp {4}"),
    ORDER_ID_PROCESSED_SUCCESS("Order {0} processed successfully"),
    ORDER_ID_PROCESSED_FAILED("Order {0} failed!"),
    ORDER_COMPLETING_PAYMENT_FOR_ID("Completing payment for order with id: {0}"),
    ORDER_PAID_FOR_ID("Order id: {0} PAID."),
    ORDER_ID_CANCELLING("Cancelling payment for order with id: {0}"),
    ORDER_ID_CANCELED_ID("Order id: {0} CANCELED."),
    ORDER_ID_APPROVING("Approving order id: [{0}]."),
    ORDER_ID_APPROVED("Order id: {0} APPROVED."),
    ORDER_ID_PUBLISHING_ORDER_CANCELLED_EVENT("Publishing OrderCancelledEvent for Oder id: {0} with failure " +
            "messages: {1}."),
    ORDER_ID_CREATED_ORDER_RESPONSE("Returning {0} with ID: {1}"),

    ORDER_ID_STATUS_UPDATED("{0} with ID [{1}] updated with STATUS : {2}"),

    ORDER_MESSAGES_RECEIVED_FOR_CLEANUP("Received {0} {1} for clean-up. Payloads: {2}"),
    ORDER_MESSAGES_DELETED("{0} {1} deleted."),

    ORDER_ERROR_WHILE_SENDING_REQUEST_AVRO("Error while sending {0} message {1} to topic {2}"),
    ORDER_ERROR_MSG_SENDING_REQ_AVRO_KAFKA("Error while sending {0} message to Kafka. orderId {1}, error: {2}"),


    // PAYMENT
    PAYMENT_REQUEST_PROCESSING_FOR_ID("Processing payment for order id: {0}"),
    PAYMENT_REQUEST_CANCELLING_FOR_ID("Cancelling payment for order id: {0}"),
    PAYMENT_REQUEST_SUCCESS_FOR_ID("Payment request success for order id: {0}!"),
    PAYMENT_REQUEST_CANCELED_FOR_ID("Payment request canceled for order id: {0}!"),
    PAYMENT_ERR_NOT_ENOUGH_CREDIT("CustomerId: {0}, does not have enough credit for payment!"),
    PAYMENT_ERR_CREDIT_HISTORY_NOT_EQUALS("Credit history total and Current credit is not equal for CustomerId: {0}!"),
    PAYMENT_ERR_FAILED_FOR_ORDER_ID("Payment request failed for order id: {0}!"),
    PAYMENT_ERR_STATUS_UNSUPPORTED("Unsupported payment order status: {0}"),
    PAYMENT_ERR_ID_NOT_FOUND("No payment found for order id: {0}"),


    // RESTAURANT
    RESTAURANT_VALIDATING_ORDER_ID("Validating Order Id {0}"),
    RESTAURANT_APPROVED_ORDER_ID("Order id {0} APPROVED."),
    RESTAURANT_REJECTED_ORDER_ID("Order id {0} REJECTED."),
    RESTAURANT_PAYMENT_NOT_COMPLETED("Payment is not completed for order: {0}"),
    RESTAURANT_PRODUCT_NOT_AVAILABLE("Product with id: {0} is not available"),
    RESTAURANT_PRODUCT_PRICE_INCORRECT("Price total is not correct for order: {0}"),

    // CUSTOMER
    CUSTOMER_ID_INITIATED("Customer id [{0}] is initiated."),
    CUSTOMER_ID_COULD_NOT_BE_SAVED("Customer id [{0}] could not be saved."),
    CUSTOMER_EVENT_ERR_SENDING_CUSTOMER_TO_KAFKA("Error while sending {0} to kafka for {1} id: [{2}]. Error: {3}."),
    CUSTOMER_EVENT_ERR_SENDING_TO_KAFKA("Error while sending message {0} to topic {1}"),
    CUSTOMER_EVENT_ON_SUCCESS("Received new metadata." +
            " Topic: {0}; Partition {1}; Offset {2}; Timestamp {3}, at time {4}"),
    CUSTOMER_CONTROLLER_CREATE("Creating customer with username: {0}");

    final String key;

    LogMessages(final String key) {
        this.key = key;
    }

    public String build(Object... objects) {
        return format(key, objects);
    }
}
