package com.h.udemy.java.uservices.payment.domain.core;

import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditEntry;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentEvent;
import com.h.udemy.java.uservices.payment.domain.core.test.config.ApiEnvTest;
import com.h.udemy.java.uservices.payment.domain.core.test.util.factory.CreditEntryFactory;
import com.h.udemy.java.uservices.payment.domain.core.test.util.factory.PaymentFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static com.h.udemy.java.uservices.payment.domain.core.test.Const.CUSTOMER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentDomainServiceTest extends ApiEnvTest {

    @Autowired
    PaymentDomainService paymentDomainService;

    @Test
    void should_validateAndInitiatePayment() {
        Payment payment = PaymentFactory.createPayment(CUSTOMER_ID);
        CreditEntry creditEntry = CreditEntryFactory.createOne(CUSTOMER_ID);

        PaymentEvent paymentEvent = paymentDomainService
                .validateAndInitiatePayment(payment,
                        creditEntry,
                        new ArrayList<>(),
                        new ArrayList<>());

        assertEquals(PaymentStatus.COMPLETED, paymentEvent.getPayment().getPaymentStatus());
    }

    @Test
    void should_return_NOK_validateAndInitiatePayment_when_payment_is_greater_than_credit() {
        Payment payment = PaymentFactory.createPayment(99_999.999);
        CreditEntry creditEntry = CreditEntryFactory.createOne();

        PaymentEvent paymentEvent = paymentDomainService
                .validateAndInitiatePayment(payment,
                        creditEntry,
                        new ArrayList<>(),
                        new ArrayList<>());

        assertEquals(PaymentStatus.FAILED, paymentEvent.getPayment().getPaymentStatus());
    }

    @Test
    void should_validateAndCancelPayment() {
        Payment payment = PaymentFactory.createPayment();
        CreditEntry creditEntry = CreditEntryFactory.createOne();

        PaymentEvent paymentEvent = paymentDomainService
                .validateAndCancelPayment(payment,
                        creditEntry,
                        new ArrayList<>(),
                        new ArrayList<>());

        assertEquals(PaymentStatus.CANCELLED, paymentEvent.getPayment().getPaymentStatus());
    }

    @Test
    void should_validateAndCancelPayment_with_Failed_status() {
        Payment payment = PaymentFactory.createPayment(-99.999);
        CreditEntry creditEntry = CreditEntryFactory.createOne();

        PaymentEvent paymentEvent = paymentDomainService
                .validateAndCancelPayment(payment,
                        creditEntry,
                        new ArrayList<>(),
                        new ArrayList<>());

        assertEquals(PaymentStatus.FAILED, paymentEvent.getPayment().getPaymentStatus());
    }
}