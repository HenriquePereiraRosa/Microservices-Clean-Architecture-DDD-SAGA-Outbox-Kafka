package com.h.udemy.java.uservices.payment.domain.service;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.payment.domain.core.PaymentDomainServiceImpl;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditEntry;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditHistory;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentEvent;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import com.h.udemy.java.uservices.payment.domain.service.exception.PaymentDomainServiceException;
import com.h.udemy.java.uservices.payment.domain.service.mapper.PaymentDataMapper;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.CreditEntryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.CreditHistoryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.messages.Messages.*;
import static java.text.MessageFormat.format;

@Slf4j
@Component
public class PaymentRequestHelper {

    private final PaymentDomainServiceImpl paymentDomainServiceImpl;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentCompletedMessagePublisher paymentCompletedEventPublisher;
    private final PaymentCancelledMessagePublisher paymentCancelledEventPublisher;
    private final PaymentFailedMessagePublisher paymentFailedEventPublisher;

    public PaymentRequestHelper(PaymentDomainServiceImpl paymentDomainServiceImpl,
                                PaymentDataMapper paymentDataMapper,
                                PaymentRepository paymentRepository,
                                CreditEntryRepository creditEntryRepository,
                                CreditHistoryRepository creditHistoryRepository,
                                PaymentCompletedMessagePublisher paymentCompletedEventPublisher,
                                PaymentCancelledMessagePublisher paymentCancelledEventPublisher,
                                PaymentFailedMessagePublisher paymentFailedEventPublisher) {
        this.paymentDomainServiceImpl = paymentDomainServiceImpl;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
        this.paymentCompletedEventPublisher = paymentCompletedEventPublisher;
        this.paymentCancelledEventPublisher = paymentCancelledEventPublisher;
        this.paymentFailedEventPublisher = paymentFailedEventPublisher;
    }

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info(format(PAYMENT_SUCCESSFULLY_RECEIVED_COMPLETE_EVENT_FOR_ID.get(),
                paymentRequest.getOrderId()));

        Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent =
                paymentDomainServiceImpl.validateAndInitiatePayment(
                        payment,
                        creditEntry,
                        creditHistories,
                        failureMessages,
                        paymentCompletedEventPublisher,
                        paymentFailedEventPublisher);

        persistDbObjects(payment, creditEntry, creditHistories, failureMessages);
        return paymentEvent;
    }

    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        final String msg = format(PAYMENT_SUCCESSFULLY_RECEIVED_CANCEL_EVENT_FOR_ID.get(),
                paymentRequest.getOrderId());
        log.info(msg);

        Optional<Payment> paymentResponse = paymentRepository
                .findByOrderId(UUID.fromString(paymentRequest.getOrderId()));

        if(paymentResponse.isEmpty()) {
            final String errMsg = format(ERR_PAYMENT_COULD_NOT_BE_FOUND_WITH_ORDER_ID.get(),
                    paymentRequest.getOrderId());
            log.info(errMsg);
            throw new PaymentDomainServiceException(errMsg);
        }

        Payment payment = paymentResponse.get();
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainServiceImpl
                .validateAndCancelPayment(
                        payment,
                        creditEntry,
                        creditHistories,
                        failureMessages,
                        paymentCancelledEventPublisher,
                        paymentFailedEventPublisher);

        persistDbObjects(payment, creditEntry, creditHistories, failureMessages);
        return paymentEvent;
    }


    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);
        if(creditEntry.isEmpty()) {
            final String msg = format(ERR_PAYMENT_COULD_NOT_FIND_CREDIT_ENTRY_FOR_CUSTOMER_ID.get(),
                    customerId.getValue());
            log.info(msg);
            throw new PaymentDomainServiceException(msg);
        }
        return creditEntry.get();
    }

    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistories = creditHistoryRepository.findByCustomerId(customerId);
        if(creditHistories.isEmpty()) {
            final String msg = format(ERR_PAYMENT_COULD_NOT_FIND_CREDIT_HISTORY_FOR_CUSTOMER_ID.get(),
                    customerId.getValue());
            log.info(msg);
            throw new PaymentDomainServiceException(msg);
        }
        return creditHistories.get();
    }

    private void persistDbObjects(Payment payment,
                                  CreditEntry creditEntry,
                                  List<CreditHistory> creditHistories,
                                  List<String> failureMessages) {
        paymentRepository.save(payment);

        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }
    }
}