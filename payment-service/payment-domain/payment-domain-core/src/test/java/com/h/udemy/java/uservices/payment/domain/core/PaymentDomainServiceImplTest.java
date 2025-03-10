package com.h.udemy.java.uservices.payment.domain.core;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditEntry;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditHistory;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCancelledEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentFailedEvent;
import com.h.udemy.java.uservices.payment.domain.core.test.config.ApiEnvTest;
import com.h.udemy.java.uservices.payment.domain.core.test.util.factory.CreditEntryFactory;
import com.h.udemy.java.uservices.payment.domain.core.test.util.factory.CreditHistoryFactory;
import com.h.udemy.java.uservices.payment.domain.core.test.util.factory.PaymentFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.h.udemy.java.uservices.constants.TestConstants.CUSTOMER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentDomainServiceImplTest extends ApiEnvTest {

    public static final double ERROR_PRICE = -99.999;
    public static final double PRICE = 99_999.999;

    @Autowired
    PaymentDomainServiceImpl paymentDomainServiceImpl;
    @MockBean
    DomainEventPublisher<PaymentCompletedEvent> completedEventPublisher;
    @MockBean
    DomainEventPublisher<PaymentCancelledEvent> cancelledEventPublisher;
    @MockBean
    DomainEventPublisher<PaymentFailedEvent> failedEventPublisher;

    @Test
    void should_validateAndInitiatePayment() {
        Payment payment = PaymentFactory.createPayment(CUSTOMER_ID);
        CreditEntry creditEntry = CreditEntryFactory.createOne(CUSTOMER_ID);
        List<CreditHistory> historyList = new ArrayList<>(CreditHistoryFactory.createOKList(1));

        PaymentEvent paymentEvent = paymentDomainServiceImpl
                .validateAndInitiatePayment(payment,
                        creditEntry,
                        historyList,
                        new ArrayList<>());

        assertEquals(PaymentStatus.COMPLETED, paymentEvent.getPayment().getPaymentStatus());
        assertTrue(CollectionUtils.isEmpty(paymentEvent.getFailureMessages()));
        assertNotNull(paymentEvent.getCreatedAt());
    }

    @Test
    void should_return_NOK_validateAndInitiatePayment_when_payment_is_greater_than_credit() {
        Payment payment = PaymentFactory.createPayment(99_999.999);
        CreditEntry creditEntry = CreditEntryFactory.createOne();

        PaymentEvent paymentEvent = paymentDomainServiceImpl
                .validateAndInitiatePayment(payment,
                        creditEntry,
                        new ArrayList<>(),
                        new ArrayList<>());

        assertEquals(PaymentStatus.FAILED, paymentEvent.getPayment().getPaymentStatus());
    }

    @Test
    void should_return_NOK_validateAndInitiatePayment_when_debit_history_is_greater() {
        Payment payment = PaymentFactory.createPayment(PRICE);
        CreditEntry creditEntry = CreditEntryFactory.createOne();

        List<CreditHistory> historyList = new ArrayList<>(CreditHistoryFactory.createNOKList());

        PaymentEvent paymentEvent = paymentDomainServiceImpl
                .validateAndInitiatePayment(payment,
                        creditEntry,
                        historyList,
                        new ArrayList<>());

        assertEquals(PaymentStatus.FAILED, paymentEvent.getPayment().getPaymentStatus());
    }

    @Test
    void should_validateAndCancelPayment() {
        Payment payment = PaymentFactory.createPayment();
        CreditEntry creditEntry = CreditEntryFactory.createOne();

        PaymentEvent paymentEvent = paymentDomainServiceImpl
                .validateAndCancelPayment(payment,
                        creditEntry,
                        new ArrayList<>(),
                        new ArrayList<>());

        assertEquals(PaymentStatus.CANCELLED, paymentEvent.getPayment().getPaymentStatus());
    }

    @Test
    void should_validateAndCancelPayment_with_Failed_status() {
        Payment payment = PaymentFactory.createPayment(ERROR_PRICE);
        CreditEntry creditEntry = CreditEntryFactory.createOne();

        PaymentEvent paymentEvent = paymentDomainServiceImpl
                .validateAndCancelPayment(payment,
                        creditEntry,
                        new ArrayList<>(),
                        new ArrayList<>());

        assertEquals(PaymentStatus.FAILED, paymentEvent.getPayment().getPaymentStatus());
    }
}