package com.h.udemy.java.uservices.payment.domain.service.outbox.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.payment.domain.core.exception.PaymentDomainException;
import com.h.udemy.java.uservices.payment.domain.service.outbox.model.OrderEventPayload;
import com.h.udemy.java.uservices.payment.domain.service.outbox.model.OrderOutboxMessage;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.OrderOutboxRepository;
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
public class OrderOutboxHelper {

    private final OrderOutboxRepository orderOutboxRepository;
    private final ObjectMapper objectMapper;

    public OrderOutboxHelper(
            OrderOutboxRepository orderOutboxRepository,
            ObjectMapper objectMapper) {
        this.orderOutboxRepository = orderOutboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public Optional<OrderOutboxMessage>
    getCompleteOrderOutboxMessageByOutboxStatusAndSagaStatus(
            UUID sagaId,
            PaymentStatus paymentStatus) {

        return orderOutboxRepository.findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(
                ORDER_SAGA_NAME,
                sagaId,
                paymentStatus,
                OutboxStatus.COMPLETED);

    }

    @Transactional(readOnly = true)
    public Optional<List<OrderOutboxMessage>> getOrderOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        return orderOutboxRepository.findByTypeAndOutboxStatus(ORDER_SAGA_NAME, outboxStatus);
    }

    public void save(OrderOutboxMessage orderOutboxMessage) {

        OrderOutboxMessage response = orderOutboxRepository.save(orderOutboxMessage);

        if (isNull(response)) {
            final String errorMsg = ERR_OUTBOX_MESSAGE_COULD_NOT_BE_SAVED.build(
                    OrderOutboxMessage.class.getSimpleName(),
                    orderOutboxMessage.getId());

            log.error(errorMsg);
            throw new PaymentDomainException(errorMsg);
        }

        log.info(OUTBOX_MESSAGE_SAVED.build(
                OrderOutboxMessage.class.getSimpleName(),
                orderOutboxMessage.getId()));

    }

    @Transactional
    public void saveOrderOutboxMessage(
            OrderEventPayload orderEventPayload,
            PaymentStatus paymentStatus,
            OutboxStatus outboxStatus,
            UUID sagaId) {

        this.save(OrderOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(orderEventPayload.getCreatedAt())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderEventPayload))
                .build());
    }

    private String createPayload(OrderEventPayload orderEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderEventPayload);
        } catch (JsonProcessingException e) {

            final String eMsg = ERR_ORDER_COULD_NOT_BE_MAPPED.build(
                    OrderEventPayload.class.getSimpleName(),
                    orderEventPayload.getOrderId());
            log.info(eMsg);
            throw new PaymentDomainException(eMsg, e);
        }
    }

    @Transactional
    public void updateOutboxStatus(OrderOutboxMessage orderOutboxMessage, OutboxStatus outboxStatus) {
        orderOutboxMessage.setOutboxStatus(outboxStatus);
        this.save(orderOutboxMessage);

        log.info(ORDER_ID_STATUS_UPDATED.build(
                OrderOutboxMessage.class.getSimpleName(),
                orderOutboxMessage.getId(),
                outboxStatus.name()));
    }

    @Transactional
    public void deleteOrderOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {

        orderOutboxRepository.deleteByTypeAndOutboxStatus(
                ORDER_SAGA_NAME,
                outboxStatus);
    }
}