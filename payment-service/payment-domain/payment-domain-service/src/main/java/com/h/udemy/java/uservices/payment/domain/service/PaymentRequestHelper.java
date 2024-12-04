package com.h.udemy.java.uservices.payment.domain.service;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.payment.domain.core.PaymentDomainService;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditEntry;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditHistory;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCancelledEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentFailedEvent;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import com.h.udemy.java.uservices.payment.domain.service.exception.PaymentDomainServiceException;
import com.h.udemy.java.uservices.payment.domain.service.mapper.PaymentDataMapper;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.ICreditEntryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.ICreditHistoryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.IPaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.messages.Messages.*;
import static java.text.MessageFormat.format;

@Slf4j
@Component
public class PaymentRequestHelper {

    private static final boolean COMPLETED_EVENT = true;
    private static final boolean CANCELLED_EVENT = false;

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final IPaymentRepository iPaymentRepository;
    private final ICreditEntryRepository iCreditEntryRepository;
    private final ICreditHistoryRepository iCreditHistoryRepository;
    private final IDomainEventPublisher<PaymentCompletedEvent> completedEventPublisher;
    private final IDomainEventPublisher<PaymentCancelledEvent> cancelledEventPublisher;
    private final IDomainEventPublisher<PaymentFailedEvent> failedEventPublisher;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentDataMapper paymentDataMapper,
                                IPaymentRepository iPaymentRepository,
                                ICreditEntryRepository iCreditEntryRepository,
                                ICreditHistoryRepository iCreditHistoryRepository,
                                IDomainEventPublisher<PaymentCompletedEvent> completedEventPublisher,
                                IDomainEventPublisher<PaymentCancelledEvent> cancelledEventPublisher,
                                IDomainEventPublisher<PaymentFailedEvent> failedEventPublisher) {
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.iPaymentRepository = iPaymentRepository;
        this.iCreditEntryRepository = iCreditEntryRepository;
        this.iCreditHistoryRepository = iCreditHistoryRepository;
        this.completedEventPublisher = completedEventPublisher;
        this.cancelledEventPublisher = cancelledEventPublisher;
        this.failedEventPublisher = failedEventPublisher;
    }

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        final String msg = format(PAYMENT_SUCCESSFULLY_RECEIVED_COMPLETE_EVENT_FOR_ID.get(),
                paymentRequest.getOrderId());
        log.info(msg);

        Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);

        return persistDbObjects(payment, COMPLETED_EVENT);
    }

    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        final String msg = format(PAYMENT_SUCCESSFULLY_RECEIVED_CANCEL_EVENT_FOR_ID.get(),
                paymentRequest.getOrderId());
        log.info(msg);

        Optional<Payment> paymentResponse = iPaymentRepository
                .findByOrderId(UUID.fromString(paymentRequest.getOrderId()));

        if(paymentResponse.isEmpty()) {
            final String errMsg = format(ERR_PAYMENT_COULD_NOT_BE_FOUND_WITH_ORDER_ID.get(),
                    paymentRequest.getOrderId());
            log.info(errMsg);
            throw new PaymentDomainServiceException(errMsg);
        }

        Payment payment = paymentResponse.get();

        return persistDbObjects(payment, CANCELLED_EVENT);
    }

    private PaymentEvent persistDbObjects(Payment payment, boolean isCompletedEvent) {
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        List<String> failureMsgs = new ArrayList<>();
        PaymentEvent paymentEvent;

        if(isCompletedEvent) {
            paymentEvent = paymentDomainService
                    .validateAndInitiatePayment(payment,
                            creditEntry,
                            creditHistories,
                            failureMsgs,
                            completedEventPublisher,
                            failedEventPublisher);
        } else {
            paymentEvent = paymentDomainService
                    .validateAndCancelPayment(payment,
                            creditEntry,
                            creditHistories,
                            failureMsgs,
                            cancelledEventPublisher,
                            failedEventPublisher);
        }

        iPaymentRepository.save(payment);
        if(CollectionUtils.isEmpty(failureMsgs)) {
            iCreditEntryRepository.save(creditEntry);
            iCreditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }

        return paymentEvent;
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntry = iCreditEntryRepository.findByCustomerId(customerId);
        if(creditEntry.isEmpty()) {
            final String msg = format(ERR_PAYMENT_COULD_NOT_FIND_CREDIT_ENTRY_FOR_CUSTOMER_ID.get(),
                    customerId.getValue());
            log.info(msg);
            throw new PaymentDomainServiceException(msg);
        }
        return creditEntry.get();
    }

    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistories = iCreditHistoryRepository.findByCustomerId(customerId);
        if(creditHistories.isEmpty()) {
            final String msg = format(ERR_PAYMENT_COULD_NOT_FIND_CREDIT_HISTORY_FOR_CUSTOMER_ID.get(),
                    customerId.getValue());
            log.info(msg);
            throw new PaymentDomainServiceException(msg);
        }
        return creditHistories.get();
    }
}