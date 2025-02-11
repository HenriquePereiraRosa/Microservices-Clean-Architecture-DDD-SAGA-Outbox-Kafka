package com.h.udemy.java.uservices.payment.domain.service;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditEntry;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditHistory;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import com.h.udemy.java.uservices.payment.domain.service.exception.PaymentDomainServiceException;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.CreditEntryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.CreditHistoryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.PaymentRepository;
import com.h.udemy.java.uservices.payment.domain.service.test.config.ApiEnvTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.messages.Messages.*;
import static com.h.udemy.java.uservices.payment.domain.service.test.util.factory.CreditEntryFactory.createOne;
import static com.h.udemy.java.uservices.payment.domain.service.test.util.factory.CreditHistoryFactory.createOKList;
import static com.h.udemy.java.uservices.payment.domain.service.test.util.factory.PaymentFactory.createPayment;
import static com.h.udemy.java.uservices.payment.domain.service.test.util.factory.PaymentRequestFactory.createPaymentRequest;
import static java.text.MessageFormat.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentRequestHelperTest extends ApiEnvTest {

    private final PaymentRequest paymentRequest = createPaymentRequest();
    private final CreditEntry creditEntry = createOne();
    private final List<CreditHistory> creditHistories = createOKList(1);
    private final Payment payment = createPayment();


    @Autowired
    PaymentRequestHelper paymentRequestHelper;

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    CreditEntryRepository creditEntryRepository;
    @Autowired
    CreditHistoryRepository creditHistoryRepository;


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

        when(creditEntryRepository.findByCustomerId(new CustomerId(UUID
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
    @Order(3)
    void should_persistPayment_when_request_is_right() {

        when(creditEntryRepository.findByCustomerId(any()))
                .thenReturn(Optional.of(creditEntry));

        when(creditHistoryRepository.findByCustomerId(any()))
                .thenReturn(Optional.of(creditHistories));

        paymentRequestHelper.persistPayment(paymentRequest);

        // todo: redo assertion without PaymentEvent obj
//        assertTrue(CollectionUtils.isEmpty(paymentEvent.getFailureMessages()));
    }

    @Test
    @Order(4)
    void should_persistCancelPayment_when_request_is_right() {

        when(paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId())))
                .thenReturn(Optional.of(payment));

        paymentRequestHelper.persistCancelPayment(paymentRequest);

        // todo: redo assertion without PaymentEvent obj

//        assertTrue(CollectionUtils.isEmpty(paymentEvent.getFailureMessages()));
    }
}