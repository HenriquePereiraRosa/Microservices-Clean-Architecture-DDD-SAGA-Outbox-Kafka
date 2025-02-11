package com.h.udemy.java.uservices.payment.domain.service;

import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_PAYMENT_COULD_NOT_BE_FOUND_WITH_ORDER_ID;
import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_PAYMENT_COULD_NOT_FIND_CREDIT_ENTRY_FOR_CUSTOMER_ID;
import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_PAYMENT_COULD_NOT_FIND_CREDIT_HISTORY_FOR_CUSTOMER_ID;
import static com.h.udemy.java.uservices.domain.messages.Messages.PAYMENT_SUCCESSFULLY_RECEIVED_CANCEL_EVENT_FOR_ID;
import static com.h.udemy.java.uservices.domain.messages.Messages.PAYMENT_SUCCESSFULLY_RECEIVED_COMPLETE_EVENT_FOR_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_MESSAGE_ALREADY_SAVED;
import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.payment.domain.core.PaymentDomainService;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentEvent;
import com.h.udemy.java.uservices.payment.domain.service.outbox.model.OrderOutboxMessage;
import com.h.udemy.java.uservices.payment.domain.service.outbox.scheduler.OrderOutboxHelper;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.PaymentResponseMessagePublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditEntry;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditHistory;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import com.h.udemy.java.uservices.payment.domain.service.exception.PaymentDomainServiceException;
import com.h.udemy.java.uservices.payment.domain.service.mapper.PaymentDataMapper;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.CreditEntryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.CreditHistoryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final OrderOutboxHelper orderOutboxHelper;
    private final PaymentResponseMessagePublisher paymentResponseMessagePublisher;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentDataMapper paymentDataMapper,
                                PaymentRepository paymentRepository,
                                CreditEntryRepository creditEntryRepository,
                                CreditHistoryRepository creditHistoryRepository,
                                OrderOutboxHelper orderOutboxHelper,
                                PaymentResponseMessagePublisher paymentResponseMessagePublisher) {
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
        this.orderOutboxHelper = orderOutboxHelper;
        this.paymentResponseMessagePublisher = paymentResponseMessagePublisher;
    }

    @Transactional
    public void persistPayment(final PaymentRequest paymentRequest) {

        if (this.publishIfOutboxMessageIsProcessedForPayment(paymentRequest, PaymentStatus.COMPLETED)) {
            log.info(format(OUTBOX_MESSAGE_ALREADY_SAVED.build(paymentRequest.getOrderId())));
            return;
        }

        log.info(format(PAYMENT_SUCCESSFULLY_RECEIVED_COMPLETE_EVENT_FOR_ID.build(paymentRequest.getOrderId())));

        final Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        final CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        final List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        final List<String> failureMessages = new ArrayList<>();
        final PaymentEvent paymentEvent =
                paymentDomainService.validateAndInitiatePayment(
                        payment,
                        creditEntry,
                        creditHistories,
                        failureMessages);

        persistDbObjects(payment, creditEntry, creditHistories, failureMessages);

        orderOutboxHelper.saveOrderOutboxMessage(
                paymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(paymentRequest.getSagaId()));
    }

    @Transactional
    public void persistCancelPayment(final PaymentRequest paymentRequest) {

        if (this.publishIfOutboxMessageIsProcessedForPayment(paymentRequest, PaymentStatus.CANCELLED)) {
            log.info(format(OUTBOX_MESSAGE_ALREADY_SAVED.build(paymentRequest.getOrderId())));
            return;
        }

        final String msg = format(PAYMENT_SUCCESSFULLY_RECEIVED_CANCEL_EVENT_FOR_ID.get(),
                paymentRequest.getOrderId());
        log.info(msg);

        final Optional<Payment> paymentResponse = paymentRepository
                .findByOrderId(UUID.fromString(paymentRequest.getOrderId()));

        if (paymentResponse.isEmpty()) {
            final String errMsg = format(ERR_PAYMENT_COULD_NOT_BE_FOUND_WITH_ORDER_ID.get(),
                    paymentRequest.getOrderId());
            log.info(errMsg);
            throw new PaymentDomainServiceException(errMsg);
        }

        final Payment payment = paymentResponse.get();
        final CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        final List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        final List<String> failureMessages = new ArrayList<>();
        final PaymentEvent paymentEvent = paymentDomainService
                .validateAndCancelPayment(
                        payment,
                        creditEntry,
                        creditHistories,
                        failureMessages);

        persistDbObjects(payment, creditEntry, creditHistories, failureMessages);

        orderOutboxHelper.saveOrderOutboxMessage(
                paymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(paymentRequest.getSagaId()));
    }


    private CreditEntry getCreditEntry(final CustomerId customerId) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);
        if (creditEntry.isEmpty()) {
            final String msg = format(ERR_PAYMENT_COULD_NOT_FIND_CREDIT_ENTRY_FOR_CUSTOMER_ID.get(),
                    customerId.getValue());
            log.info(msg);
            throw new PaymentDomainServiceException(msg);
        }
        return creditEntry.get();
    }

    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistories = creditHistoryRepository.findByCustomerId(customerId);
        if (creditHistories.isEmpty()) {
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

    private boolean publishIfOutboxMessageIsProcessedForPayment(PaymentRequest paymentRequest,
                                                                PaymentStatus paymentStatus) {
        Optional<OrderOutboxMessage> orderOutboxMessage =
                orderOutboxHelper.getCompleteOrderOutboxMessageByOutboxStatusAndSagaStatus(
                        UUID.fromString(paymentRequest.getSagaId()),
                        paymentStatus);

        if (orderOutboxMessage.isPresent()) {
            paymentResponseMessagePublisher.publish(
                    orderOutboxMessage.get(),
                    orderOutboxHelper::updateOutboxMessageStatus);
            return true;
        }

        return false;
    }
}