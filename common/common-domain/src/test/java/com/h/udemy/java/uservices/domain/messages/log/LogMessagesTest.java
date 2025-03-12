package com.h.udemy.java.uservices.domain.messages.log;

import com.h.udemy.java.uservices.domain.ApiEnvTestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.time.Instant;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;
import static java.text.MessageFormat.format;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class LogMessagesTest extends ApiEnvTestConfig {

    private static final UUID ID = UUID.fromString("02a0e281-8bd2-46f5-938e-289143a346a1");
    private static final String topicName = "topicName";
    private static final int partition = 3;
    private static final int offset = 2;
    private static final Instant timestamp = Instant.now();
    private static final String errorMsg = "Error Message Test";
    private static final String AVRO_MODEL_NAME= "TestRequestAvroModel";

    @Test
    void assure_that_ORDER_RECEIVED_ID_is_ok() {
        final String expected = "Received OrderCreatedEvent. orderId: 02a0e281-8bd2-46f5-938e-289143a346a1";
        final String sut = ORDER_RECEIVED_ID.build(ID);
        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

    @Test
    void assure_that_ORDER_CANCEL_RECEIVED_ID_is_ok() {
        final String expected = "Received OrderCancelEvent. orderId: 02a0e281-8bd2-46f5-938e-289143a346a1";
        final String sut = ORDER_CANCEL_RECEIVED_ID.build(ID);
        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

    @Test
    void assure_that_ORDER_SUCCESSFUL_RESPONSE_KAFKA_is_ok() {
        final String expected = "PaymentRequestAvroModel sent to Kafka for order" +
                " id: 02a0e281-8bd2-46f5-938e-289143a346a1";
        final String sut = ORDER_SUCCESSFUL_RESPONSE_KAFKA.build(ID);
        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

    @Test
    void assure_that_ORDER_SENT_REQUEST_KAFKA_is_ok() {
        final Object[] objects = {ID,
                topicName,
                partition,
                offset,
                timestamp};
        final String expected = format("Received successful response from Kafka for order id: {0}," +
                        " Topic: {1}, Partition: {2}, Offset: {3}, Timestamp {4}",
                objects);
        final String sut = ORDER_SENT_REQUEST_KAFKA.build(objects);
        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

    @Test
    void assure_that_ORDER_ID_PROCESSED_SUCCESS_is_ok() {
        final String expected = "Order 02a0e281-8bd2-46f5-938e-289143a346a1 processed successfully";
        final String sut = ORDER_ID_PROCESSED_SUCCESS.build(ID);
        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

    @Test
    void assure_that_ORDER_ID_PROCESSED_FAILED_is_ok() {
        final String expected = "Order 02a0e281-8bd2-46f5-938e-289143a346a1 failed!";
        final String sut = ORDER_ID_PROCESSED_FAILED.build(ID);
        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

    @Test
    void assure_that_KAFKA_X_REQUESTS_RECEIVED_is_ok() {

        final int messagesSize = 20;
        final String modelTestName = "MODEL_TEST_NAME";
        final int keys = 19;
        final Object[] objects = {
                messagesSize,
                modelTestName,
                keys,
                partition,
                offset};

        final String expected = format("{0} of {1} requests received with" +
                        " keys: {2}, partitions: {3}, offsets: {4}",
                objects);
        final String sut = KAFKA_X_REQUESTS_RECEIVED.build(objects);

        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

    @Test
    void assure_that_ORDER_ERROR_WHILE_SENDING_REQUEST_AVRO_is_ok() {
        final Object[] objects = {AVRO_MODEL_NAME,
                "Test",
                topicName};
        final String expected = format("Error while sending {0} message {1} to topic {2}",
                objects);
        final String sut = ORDER_ERROR_WHILE_SENDING_REQUEST_AVRO.build(objects);
        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

    @Test
    void assure_that_ORDER_ERROR_expected_SENDING_REQ_AVRO_KAFKA_is_ok() {
        final Object[] objects = {AVRO_MODEL_NAME,
                ID,
                errorMsg};
        final String expected = format("Error while sending {0} message to Kafka. orderId {1}, error: {2}",
                objects);
        final String sut = ORDER_ERROR_MSG_SENDING_REQ_AVRO_KAFKA.build(objects);
        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

    @Test
    void assure_that_PAYMENT_REQUEST_SUCCESS_FOR_ID_is_ok() {
        final String expected = "Payment request success for order id: 02a0e281-8bd2-46f5-938e-289143a346a1!";
        final String sut = PAYMENT_REQUEST_SUCCESS_FOR_ID.build(ID);

        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

    @Test
    void assure_that_PAYMENT_REQUEST_CANCELED_FOR_ID_is_ok() {
        final String expected = "Payment request canceled for order id: 02a0e281-8bd2-46f5-938e-289143a346a1!";
        final String sut = PAYMENT_REQUEST_CANCELED_FOR_ID.build(ID);

        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

    @Test
    void assure_that_PAYMENT_ERR_NOT_ENOUGH_CREDIT_is_ok() {
        final String expected = "CustomerId: 02a0e281-8bd2-46f5-938e-289143a346a1, " +
                "does not have enough credit for payment!";
        final String sut = PAYMENT_ERR_NOT_ENOUGH_CREDIT.build(ID);

        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

    @Test
    void assure_that_PAYMENT_ERR_CREDIT_HISTORY_NOT_EQUALS_is_ok() {
        final String expected = "Credit history total and Current credit is not equal for " +
                "CustomerId: 02a0e281-8bd2-46f5-938e-289143a346a1!";
        final String sut = PAYMENT_ERR_CREDIT_HISTORY_NOT_EQUALS.build(ID);

        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

    @Test
    void assure_that_PAYMENT_ERR_FAILED_FOR_ORDER_ID_is_ok() {
        final String expected = "Payment request failed for order id: 02a0e281-8bd2-46f5-938e-289143a346a1!";
        final String sut = PAYMENT_ERR_FAILED_FOR_ORDER_ID.build(ID);

        assertTrue(StringUtils.isNotBlank(sut));
        assertEquals(expected, sut);
        log.info(sut);
    }

}