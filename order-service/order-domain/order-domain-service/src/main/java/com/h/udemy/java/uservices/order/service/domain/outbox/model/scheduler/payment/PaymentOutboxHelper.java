package com.h.udemy.java.uservices.order.service.domain.outbox.model.scheduler.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.h.udemy.java.uservices.domain.valueobject.OrderStatus;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainException;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
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

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;
import static com.h.udemy.java.uservices.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static java.util.Objects.isNull;

@Slf4j
@Component
public class PaymentOutboxHelper {

    private final PaymentOutboxRepository paymentOutboxRepository;
    private final ObjectMapper objectMapper;

    public PaymentOutboxHelper(
            PaymentOutboxRepository paymentOutboxRepository,
            ObjectMapper objectMapper) {

        this.paymentOutboxRepository = paymentOutboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderPaymentOutboxMessage>>
    getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatuses) {

        return paymentOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(
                ORDER_SAGA_NAME,
                outboxStatus,
                sagaStatuses);

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
            final String errorMsg = ERR_OUTBOX_MESSAGE_COULD_NOT_BE_SAVED.build(
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
    public void savePaymentOutboxMessage(
            OrderPaymentEventPayload paymentEventPayload,
            OrderStatus orderStatus,
            SagaStatus sagaStatus,
            OutboxStatus outboxStatus,
            UUID sagaId) {

        this.save(OrderPaymentOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(paymentEventPayload.getCreatedAt())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(paymentEventPayload))
                .orderStatus(orderStatus)
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    private String createPayload(OrderPaymentEventPayload paymentEventPayload) {
        try {
            return objectMapper.writeValueAsString(paymentEventPayload);
        } catch (JsonProcessingException e) {

            final String eMsg = ERR_ORDER_COULD_NOT_BE_MAPPED.build(
                    OrderPaymentEventPayload.class.getSimpleName(),
                    paymentEventPayload.getOrderId());
            log.info(eMsg);
            throw new OrderDomainException(eMsg, e);
        }
    }

    @Transactional
    public void delete(
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatuses) {

        paymentOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(
                ORDER_SAGA_NAME,
                outboxStatus,
                sagaStatuses);
    }
}
