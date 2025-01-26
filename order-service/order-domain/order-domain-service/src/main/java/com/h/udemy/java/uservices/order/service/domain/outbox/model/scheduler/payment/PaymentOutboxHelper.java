package com.h.udemy.java.uservices.order.service.domain.outbox.model.scheduler.payment;

import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainException;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.OutboxProcessor;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.PaymentOutboxRepository;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.*;
import static com.h.udemy.java.uservices.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static java.util.Objects.isNull;

@Slf4j
@Component
public class PaymentOutboxHelper {

    private final PaymentOutboxRepository paymentOutboxRepository;

    public PaymentOutboxHelper(PaymentOutboxRepository paymentOutboxRepository) {
        this.paymentOutboxRepository = paymentOutboxRepository;
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderPaymentOutboxMessage>>
    getPaymentOutboxMessageByOutboxStatusAndSagaStatus(OutboxProcessor outboxProcessor) {

        return paymentOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(
                ORDER_SAGA_NAME,
                outboxProcessor.outboxStatus(),
                outboxProcessor.sagaStatuses());

    }

    @Transactional(readOnly = true)
    public Optional<OrderPaymentOutboxMessage>
    getPaymentOutboxMessageBySagaIdAndSagaStatus(
            UUID uuid,
            SagaStatus... sagaStatuses) {

        return paymentOutboxRepository.findByTypeAndSagaIdAndSagaStatus(
                ORDER_SAGA_NAME,
                uuid,
                sagaStatuses);

    }

    @Transactional
    public void save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {

        OrderPaymentOutboxMessage response = paymentOutboxRepository.save(orderPaymentOutboxMessage);

        if (isNull(response)) {
            final String errorMsg = OUTBOX_MESSAGE_COULDNT_BE_SAVED.build(
                    OrderPaymentOutboxMessage.class.getSimpleName(),
                    orderPaymentOutboxMessage.getId());

            log.error(errorMsg);
            throw new OrderDomainException(errorMsg);
        }

        log.info(OUTBOX_MESSAGE_SAVED.build(
                OrderPaymentOutboxMessage.class.getSimpleName(),
                orderPaymentOutboxMessage.getId()));

    }

    @Transactional
    public void delete(OutboxProcessor outboxProcessor) {

        paymentOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(
                        ORDER_SAGA_NAME,
                outboxProcessor.outboxStatus(),
                outboxProcessor.sagaStatuses());
    }
}
