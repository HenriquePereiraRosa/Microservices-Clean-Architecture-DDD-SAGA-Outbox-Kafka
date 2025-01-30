package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.domain.messages.Messages;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
import com.h.udemy.java.uservices.order.service.domain.mapper.OrderDataMapper;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.scheduler.payment.PaymentOutboxHelper;
import com.h.udemy.java.uservices.order.service.domain.saga.helper.OrderSagaHelper;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.h.udemy.java.uservices.domain.messages.Messages.ORDER_ID_CREATED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CREATED_ORDER_RESPONSE;
import static com.h.udemy.java.uservices.saga.strategy.SagaStatusStrategyContext.getSagaStatusFromOrderStatus;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final PaymentOutboxHelper paymentOutboxHelper;

    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper,
                                     OrderDataMapper orderDataMapper,
                                     PaymentOutboxHelper paymentOutboxHelper) {

        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.paymentOutboxHelper = paymentOutboxHelper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent =
                orderCreateHelper.persistOrder(createOrderCommand);

        log.info(ORDER_ID_CREATED.build(orderCreatedEvent.getOrder().getId()));

        CreateOrderResponse createOrderResponse = orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(),
                Messages.ORDER_CREATED_SUCCESSFULLY.get());

        paymentOutboxHelper.savePaymentOutboxMessage(
                orderDataMapper.orderCreatedEventToOrderPaymentEventPayload(orderCreatedEvent),
                orderCreatedEvent.getOrder().getOrderStatus(),
                getSagaStatusFromOrderStatus(orderCreatedEvent.getOrder().getOrderStatus()),
                OutboxStatus.STARTED,
                UUID.randomUUID());

        log.info(ORDER_ID_CREATED_ORDER_RESPONSE.build(
                CreateOrderResponse.class.getSimpleName(),
                orderCreatedEvent.getOrder().getId()));

        return createOrderResponse;

    }
}
