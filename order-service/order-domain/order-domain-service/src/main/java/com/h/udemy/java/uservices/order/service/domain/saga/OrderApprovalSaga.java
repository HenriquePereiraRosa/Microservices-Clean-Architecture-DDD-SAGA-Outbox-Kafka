package com.h.udemy.java.uservices.order.service.domain.saga;

import static com.h.udemy.java.uservices.domain.Constants.getZonedDateTimeNow;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_APPROVED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_APPROVING;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELED_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELLING;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_MESSAGE_ALREADY_PROCESSED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_MESSAGE_COULD_NOT_BE_FOUND;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_MESSAGE_SAGA_ID_ALREADY_ROLLED_BACK;
import static com.h.udemy.java.uservices.saga.strategy.SagaStatusStrategyContext.getSagaStatusFromOrderStatus;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.h.udemy.java.uservices.domain.valueobject.OrderStatus;
import com.h.udemy.java.uservices.order.service.domain.OrderDomainService;
import com.h.udemy.java.uservices.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCancelledEvent;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainException;
import com.h.udemy.java.uservices.order.service.domain.mapper.OrderDataMapper;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.scheduler.approval.ApprovalOutboxHelper;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.scheduler.payment.PaymentOutboxHelper;
import com.h.udemy.java.uservices.order.service.domain.saga.helper.OrderSagaHelper;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;
import com.h.udemy.java.uservices.saga.SagaStep;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper sagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final OrderDataMapper orderDataMapper;

    public OrderApprovalSaga(
            OrderDomainService orderDomainService,
            OrderSagaHelper sagaHelper,
            PaymentOutboxHelper paymentOutboxHelper,
            ApprovalOutboxHelper approvalOutboxHelper,
            OrderDataMapper orderDataMapper) {

        this.orderDomainService = orderDomainService;
        this.sagaHelper = sagaHelper;
        this.paymentOutboxHelper = paymentOutboxHelper;
        this.approvalOutboxHelper = approvalOutboxHelper;
        this.orderDataMapper = orderDataMapper;
    }

    @Override
    @Transactional
    public void process(RestaurantApprovalResponse restaurantApprovalResponse) {

        Optional<OrderApprovalOutboxMessage> approvalOutboxMessage =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(restaurantApprovalResponse.getSagaId()),
                        SagaStatus.PROCESSING);

        if (approvalOutboxMessage.isEmpty()) {
            log.info(OUTBOX_MESSAGE_ALREADY_PROCESSED.build(
                    OrderPaymentOutboxMessage.class.getSimpleName(),
                    restaurantApprovalResponse.getSagaId()));
            return;
        }

        Order order = approveOrder(restaurantApprovalResponse);
        SagaStatus sagaStatus = getSagaStatusFromOrderStatus(order.getOrderStatus());
        approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(
                approvalOutboxMessage.get(),
                order.getOrderStatus(),
                sagaStatus));

        paymentOutboxHelper.save(
                getUpdatedPaymentOutboxMessage(
                        restaurantApprovalResponse.getSagaId(),
                        order.getOrderStatus(),
                        sagaStatus));

        log.info(ORDER_ID_APPROVED.build(restaurantApprovalResponse.getOrderId()));
    }

    private Order approveOrder(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info(ORDER_ID_APPROVING.build(restaurantApprovalResponse.getOrderId()));
        Order order = sagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        orderDomainService.approveOrder(order);
        sagaHelper.saveOrder(order);
        return order;
    }

    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(
            OrderApprovalOutboxMessage approvalOutboxMessage,
            OrderStatus orderStatus,
            SagaStatus sagaStatus) {

        approvalOutboxMessage.setProcessedAt(getZonedDateTimeNow());
        approvalOutboxMessage.setOrderStatus(orderStatus);
        approvalOutboxMessage.setSagaStatus(sagaStatus);
        return approvalOutboxMessage;
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(
            String sagaId,
            OrderStatus orderStatus,
            SagaStatus sagaStatus) {

        Optional<OrderPaymentOutboxMessage> paymentOutboxMessageResponse =
                paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(sagaId),
                        SagaStatus.PROCESSING);

        if (paymentOutboxMessageResponse.isEmpty()) {
            throw new OrderDomainException(OUTBOX_MESSAGE_COULD_NOT_BE_FOUND.build(
                    OrderPaymentOutboxMessage.class.getSimpleName(),
                    SagaStatus.PROCESSING.name()));
        }

        OrderPaymentOutboxMessage orderPaymentOutboxMessage = paymentOutboxMessageResponse.get();
        orderPaymentOutboxMessage.setProcessedAt(getZonedDateTimeNow());
        orderPaymentOutboxMessage.setOrderStatus(orderStatus);
        orderPaymentOutboxMessage.setSagaStatus(sagaStatus);
        return orderPaymentOutboxMessage;
    }

    @Override
    @Transactional
    public void rollback(RestaurantApprovalResponse restaurantApprovalResponse) {

        Optional<OrderApprovalOutboxMessage> approvalOutboxMessage =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(restaurantApprovalResponse.getSagaId()),
                        SagaStatus.PROCESSING);

        if (approvalOutboxMessage.isEmpty()) {
            log.info(OUTBOX_MESSAGE_SAGA_ID_ALREADY_ROLLED_BACK
                    .build(restaurantApprovalResponse.getSagaId()));
            return;
        }
        OrderCancelledEvent orderCancelledEvent = rollbackOrder(restaurantApprovalResponse);
        SagaStatus sagaStatus =
                getSagaStatusFromOrderStatus(orderCancelledEvent.getOrder().getOrderStatus());

        approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(
                approvalOutboxMessage.get(),
                orderCancelledEvent.getOrder().getOrderStatus(),
                sagaStatus));

        paymentOutboxHelper.savePaymentOutboxMessage(
                orderDataMapper.orderCancelledEventToOrderPaymentEventPayload(orderCancelledEvent),
                orderCancelledEvent.getOrder().getOrderStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(restaurantApprovalResponse.getSagaId()));

        log.info(ORDER_ID_CANCELED_ID.build(restaurantApprovalResponse.getOrderId()));
    }

    private OrderCancelledEvent rollbackOrder(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info(ORDER_ID_CANCELLING.build(restaurantApprovalResponse.getOrderId()));
        Order order = sagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        OrderCancelledEvent orderCancelledEvent =  orderDomainService.cancelOrderPayment(
                order,
                restaurantApprovalResponse.getFailureMessages());

        sagaHelper.saveOrder(order);
        return orderCancelledEvent;
    }
}
