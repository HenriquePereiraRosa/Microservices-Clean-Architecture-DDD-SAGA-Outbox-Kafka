package com.h.udemy.java.uservices.payment.domain.service;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.payment.domain.core.PaymentDomainService;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditEntry;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditHistory;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCancelledEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentFailedEvent;
import com.h.udemy.java.uservices.payment.domain.service.test.config.ApiEnvTest;
import com.h.udemy.java.uservices.payment.domain.service.test.util.factory.CreditEntryFactory;
import com.h.udemy.java.uservices.payment.domain.service.test.util.factory.CreditHistoryFactory;
import com.h.udemy.java.uservices.payment.domain.service.test.util.factory.PaymentFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.h.udemy.java.uservices.domain.test.constants.ConstantsTest.ERROR_PRICE;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentRequestMessageListenerTest extends ApiEnvTest {

    @Autowired
    PaymentDomainService paymentDomainService;
    @Autowired
    IDomainEventPublisher<PaymentCompletedEvent> completedEventPublisher;
    @Autowired
    IDomainEventPublisher<PaymentCancelledEvent> cancelledEventPublisher;
    @Autowired
    IDomainEventPublisher<PaymentFailedEvent> failedEventPublisher;

    @Test
    void should_completePayment() {
        Payment payment = PaymentFactory.createPayment();
        CreditEntry creditEntry = CreditEntryFactory.createOne();
        List<CreditHistory> creditHistories = CreditHistoryFactory.createOKList();
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(payment,
                creditEntry,
                creditHistories,
                failureMessages,
                completedEventPublisher,
                failedEventPublisher);

        assertTrue(CollectionUtils.isEmpty(paymentEvent.getFailureMessages()));
        assertEquals(PaymentStatus.COMPLETED, paymentEvent.getPayment().getPaymentStatus());
    }

    @Test
    void should_return_FAILED_in_completePayment_when_failureMessages_is_not_null() {
        Payment payment = PaymentFactory.createPayment(ERROR_PRICE);
        CreditEntry creditEntry = CreditEntryFactory.createOne();
        List<CreditHistory> creditHistories = CreditHistoryFactory.createOKList();
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(payment,
                creditEntry,
                creditHistories,
                failureMessages,
                completedEventPublisher,
                failedEventPublisher);

        assertFalse(CollectionUtils.isEmpty(paymentEvent.getFailureMessages()));
        assertEquals(PaymentStatus.FAILED, paymentEvent.getPayment().getPaymentStatus());
    }

    @Test
    void should_cancelPayment() {
        Payment payment = PaymentFactory.createPayment();
        CreditEntry creditEntry = CreditEntryFactory.createOne();
        List<CreditHistory> creditHistories = CreditHistoryFactory.createOKList();
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(payment,
                creditEntry,
                creditHistories,
                failureMessages,
                cancelledEventPublisher,
                failedEventPublisher);

        assertTrue(CollectionUtils.isEmpty(paymentEvent.getFailureMessages()));
        assertEquals(PaymentStatus.CANCELLED, paymentEvent.getPayment().getPaymentStatus());
    }

    @Test
    void should_return_FAILED_in_cancelPayment_when_failureMessages_is_not_null() {
        Payment payment = PaymentFactory.createPayment(ERROR_PRICE);
        CreditEntry creditEntry = CreditEntryFactory.createOne();
        List<CreditHistory> creditHistories = CreditHistoryFactory.createOKList();
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(payment,
                creditEntry,
                creditHistories,
                failureMessages,
                cancelledEventPublisher,
                failedEventPublisher);

        assertFalse(CollectionUtils.isEmpty(paymentEvent.getFailureMessages()));
        assertEquals(PaymentStatus.FAILED, paymentEvent.getPayment().getPaymentStatus());
    }
}