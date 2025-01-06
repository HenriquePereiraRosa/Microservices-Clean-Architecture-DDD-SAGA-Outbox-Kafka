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
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.IPaymentCancelledMessagePublisher;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.IPaymentCompletedMessagePublisher;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.IPaymentFailedMessagePublisher;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.ICreditEntryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.ICreditHistoryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.IPaymentRepository;
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

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final IPaymentRepository paymentRepository;
    private final ICreditEntryRepository creditEntryRepository;
    private final ICreditHistoryRepository creditHistoryRepository;
    private final IPaymentCompletedMessagePublisher paymentCompletedEventPublisher;
    private final IPaymentCancelledMessagePublisher paymentCancelledEventPublisher;
    private final IPaymentFailedMessagePublisher paymentFailedEventPublisher;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentDataMapper paymentDataMapper,
                                IPaymentRepository paymentRepository,
                                ICreditEntryRepository creditEntryRepository,
                                ICreditHistoryRepository creditHistoryRepository,
                                IPaymentCompletedMessagePublisher paymentCompletedEventPublisher,
                                IPaymentCancelledMessagePublisher paymentCancelledEventPublisher,
                                IPaymentFailedMessagePublisher paymentFailedEventPublisher) {
        this.paymentDomainService = paymentDomainService;
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
                paymentDomainService.validateAndInitiatePayment(
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
        PaymentEvent paymentEvent = paymentDomainService
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