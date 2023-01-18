package com.h.udemy.java.uservices.payment.domain.core.entity;

import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.payment.domain.core.test.config.ApiEnvTest;
import com.h.udemy.java.uservices.payment.domain.core.test.util.factory.PaymentFactory;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PaymentTest extends ApiEnvTest {

    @Test
    void initializePayment_OK() {
        Payment sut = PaymentFactory.createPayment();
        sut.initializePayment();

        assertFalse(Objects.isNull(sut.getId()));
        assertFalse(Objects.isNull(sut.getCreatedAt()));
        assertFalse(Objects.isNull(sut.getPrice()));
    }

    @Test
    void validatePayment_OK() {
        Payment sut = PaymentFactory.createPayment();
        sut.initializePayment();
        sut.validatePaymentReturningFailuresMsgs();

        assertFalse(Objects.isNull(sut.getId()));
        assertFalse(Objects.isNull(sut.getCreatedAt()));
        assertFalse(Objects.isNull(sut.getPrice()));
    }

    @Test
    void validatePayment_NOK() {
        Payment sut = PaymentFactory.createPayment(-22.00);
        sut.initializePayment();
        String failureMessages = sut.validatePaymentReturningFailuresMsgs();

        assertFalse(Objects.isNull(failureMessages));
    }

    @Test
    void updateStatus_OK_return_cancelled() {
        Payment sut = PaymentFactory.createPayment();
        sut.initializePayment();
        sut.validatePaymentReturningFailuresMsgs();

        sut.updateStatus(PaymentStatus.CANCELLED);

        assertEquals(PaymentStatus.CANCELLED, sut.getPaymentStatus());
    }

    @Test
    void updateStatus_OK_return_completed() {
        Payment sut = PaymentFactory.createPayment();
        sut.initializePayment();
        sut.validatePaymentReturningFailuresMsgs();

        sut.updateStatus(PaymentStatus.COMPLETED);

        assertEquals(PaymentStatus.COMPLETED, sut.getPaymentStatus());
    }
}