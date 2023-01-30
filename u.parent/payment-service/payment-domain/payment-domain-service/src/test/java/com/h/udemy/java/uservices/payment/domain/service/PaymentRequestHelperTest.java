package com.h.udemy.java.uservices.payment.domain.service;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditEntry;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditHistory;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCancelledEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentFailedEvent;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import com.h.udemy.java.uservices.payment.domain.service.exception.PaymentDomainServiceException;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.ICreditEntryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.ICreditHistoryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.IPaymentRepository;
import com.h.udemy.java.uservices.payment.domain.service.test.config.ApiEnvTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_PAYMENT_COULD_NOT_BE_FOUND_WITH_ORDER_ID;
import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_PAYMENT_COULD_NOT_FIND_CREDIT_ENTRY_FOR_CUSTOMER_ID;
import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_PAYMENT_COULD_NOT_FIND_CREDIT_HISTORY_FOR_CUSTOMER_ID;
import static com.h.udemy.java.uservices.payment.domain.service.test.util.factory.CreditEntryFactory.createOne;
import static com.h.udemy.java.uservices.payment.domain.service.test.util.factory.CreditHistoryFactory.createOKList;
import static com.h.udemy.java.uservices.payment.domain.service.test.util.factory.PaymentFactory.createPayment;
import static com.h.udemy.java.uservices.payment.domain.service.test.util.factory.PaymentRequestFactory.createPaymentRequest;
import static java.text.MessageFormat.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentRequestHelperTest extends ApiEnvTest {

    public static final double ERROR_PRICE = -99.999;
    public static final double PRICE = 99_999.999;


    private final PaymentRequest paymentRequest = createPaymentRequest();
    private final CreditEntry creditEntry = createOne();
    private final List<CreditHistory> creditHistories = createOKList();
    private final Payment payment = createPayment();


    @Autowired
    PaymentRequestHelper paymentRequestHelper;
    @Autowired
    IDomainEventPublisher<PaymentCompletedEvent> completedEventPublisher;
    @Autowired
    IDomainEventPublisher<PaymentCancelledEvent> cancelledEventPublisher;
    @Autowired
    IDomainEventPublisher<PaymentFailedEvent> failedEventPublisher;

    @Autowired
    IPaymentRepository iPaymentRepository;
    @Autowired
    ICreditEntryRepository iCreditEntryRepository;
    @Autowired
    ICreditHistoryRepository iCreditHistoryRepository;

    @BeforeAll
    public void setup(){
    }

    @Test
    @Order(0)
    void should_throw_with_non_valid_CustomerId() {
        final Exception exception = assertThrows(PaymentDomainServiceException.class, () -> {
            paymentRequestHelper.persistPayment(paymentRequest);
        });

        final String expectedExceptionMsg = format(ERR_PAYMENT_COULD_NOT_FIND_CREDIT_ENTRY_FOR_CUSTOMER_ID.get(),
                paymentRequest.getCustomerId());

        assertEquals(expectedExceptionMsg, exception.getMessage());
    }

    @Test
    @Order(1)
    void should_throw_when_OrderId_is_NOK() {
        final Exception exception = assertThrows(PaymentDomainServiceException.class, () -> {
            paymentRequestHelper.persistCancelPayment(paymentRequest);
        });

        final String expectedExceptionMsg = format(ERR_PAYMENT_COULD_NOT_BE_FOUND_WITH_ORDER_ID.get(),
                paymentRequest.getOrderId());

        assertEquals(expectedExceptionMsg, exception.getMessage());
    }

    @Test
    @Order(2)
    void should_throw_when_customer_id_is_NOK() {

        when(iCreditEntryRepository.findByCustomerId(new CustomerId(UUID
                        .fromString(paymentRequest.getCustomerId()))))
                .thenReturn(Optional.of(creditEntry));

        final Exception exception = assertThrows(PaymentDomainServiceException.class, () -> {
            paymentRequestHelper.persistPayment(paymentRequest);
        });

        final String expectedExceptionMsg = format(ERR_PAYMENT_COULD_NOT_FIND_CREDIT_HISTORY_FOR_CUSTOMER_ID.get(),
                paymentRequest.getCustomerId());

        assertEquals(expectedExceptionMsg, exception.getMessage());
    }

    @Test
    @Order(2)
    void should_persistPayment_when_request_is_right() {

        when(iCreditHistoryRepository.findByCustomerId(new CustomerId(UUID
                        .fromString(paymentRequest.getCustomerId()))))
                .thenReturn(Optional.of(creditHistories));

        final PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequest);

        assertTrue(CollectionUtils.isEmpty(paymentEvent.getFailureMessages()));
    }

    @Test
    @Order(3)
    void should_persistCancelPayment_when_request_is_right() {

        when(iPaymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId())))
                .thenReturn(Optional.of(payment));

        PaymentEvent paymentEvent = paymentRequestHelper.persistCancelPayment(paymentRequest);

        assertTrue(CollectionUtils.isEmpty(paymentEvent.getFailureMessages()));
    }
}