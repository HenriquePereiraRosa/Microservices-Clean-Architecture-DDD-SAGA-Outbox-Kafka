package com.h.udemy.java.uservices.domain.messages.log;

import com.h.udemy.java.uservices.domain.ApiEnvTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;
import static org.junit.jupiter.api.Assertions.*;

class LogMessagesTest extends ApiEnvTestConfig {

    @Test
    void get_ORDER_RECEIVED_ID_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(ORDER_RECEIVED_ID.get()));
    }

    @Test
    void get_ORDER_CANCEL_RECEIVED_ID_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(ORDER_CANCEL_RECEIVED_ID.get()));
    }

    @Test
    void get_ORDER_SUCCESSFUL_RESPONSE_KAFKA_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(ORDER_SUCCESSFUL_RESPONSE_KAFKA.get()));
    }

    @Test
    void get_ORDER_SENT_REQUEST_KAFKA_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(ORDER_SENT_REQUEST_KAFKA.get()));
    }

    @Test
    void get_ORDER_ID_PROCESSED_SUCCESS_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(ORDER_ID_PROCESSED_SUCCESS.get()));
    }

    @Test
    void get_ORDER_ID_PROCESSED_FAILED_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(ORDER_ID_PROCESSED_FAILED.get()));
    }

    @Test
    void get_ORDER_KAFKA_NUMBER_MODEL_RESPONSES_RECEIVED_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(ORDER_KAFKA_NUMBER_MODEL_RESPONSES_RECEIVED.get()));
    }

    @Test
    void get_ORDER_ERROR_WHILE_SENDING_REQUEST_AVRO_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(ORDER_ERROR_WHILE_SENDING_REQUEST_AVRO.get()));
    }

    @Test
    void get_ORDER_ERROR_MSG_SENDING_REQ_AVRO_KAFKA_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(ORDER_ERROR_MSG_SENDING_REQ_AVRO_KAFKA.get()));
    }

    @Test
    void get_PAYMENT_REQUEST_SUCCESS_FOR_ID_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(PAYMENT_REQUEST_SUCCESS_FOR_ID.get()));
    }

    @Test
    void get_PAYMENT_REQUEST_CANCELED_FOR_ID_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(PAYMENT_REQUEST_CANCELED_FOR_ID.get()));
    }

    @Test
    void get_PAYMENT_ERR_NOT_ENOUGH_CREDIT_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(PAYMENT_ERR_NOT_ENOUGH_CREDIT.get()));
    }

    @Test
    void get_PAYMENT_ERR_CREDIT_HISTORY_NOT_EQUALS_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(PAYMENT_ERR_CREDIT_HISTORY_NOT_EQUALS.get()));
    }

    @Test
    void get_PAYMENT_ERR_FAILED_FOR_ORDER_ID_is_not_empty() {
        assertTrue(StringUtils.isNotBlank(PAYMENT_ERR_FAILED_FOR_ORDER_ID.get()));
    }

}