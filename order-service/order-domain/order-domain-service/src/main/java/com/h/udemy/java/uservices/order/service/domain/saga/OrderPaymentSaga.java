package com.h.udemy.java.uservices.order.service.domain.saga;

import static com.h.udemy.java.uservices.domain.Constants.getZonedDateTimeNow;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_COMPLETING_PAYMENT_FOR_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELED_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELLING;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_PAID_FOR_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_MESSAGE_ALREADY_PROCESSED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_MESSAGE_COULD_NOT_BE_FOUND;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_MESSAGE_SAGA_ID_ALREADY_ROLLED_BACK;
import static com.h.udemy.java.uservices.order.service.domain.saga.strategy.OrderPaymentSagaStatusStrategyContext.getSagaStatusFromPaymentStatus;
import static com.h.udemy.java.uservices.saga.strategy.SagaStatusStrategyContext.getSagaStatusFromOrderStatus;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.h.udemy.java.uservices.domain.valueobject.OrderStatus;
import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.order.service.domain.OrderDomainService;
import com.h.udemy.java.uservices.order.service.domain.dto.message.PaymentResponse;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;
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
public class OrderPaymentSaga implements SagaStep<PaymentResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper sagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final OrderDataMapper orderDataMapper;

    public OrderPaymentSaga(
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
    public void process(PaymentResponse paymentResponse) {
        Optional<OrderPaymentOutboxMessage> paymentOutboxMessage =
                paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(paymentResponse.getSagaId()),
                        SagaStatus.STARTED);

        if (paymentOutboxMessage.isEmpty()) {
            log.info(OUTBOX_MESSAGE_ALREADY_PROCESSED.build(
                    OrderPaymentOutboxMessage.class.getSimpleName(),
                    paymentResponse.getSagaId()));
            return;
        }

        OrderPaidEvent orderPaidEvent = completePaymentForOrder(paymentResponse);

        SagaStatus sagaStatus =
                getSagaStatusFromOrderStatus(orderPaidEvent.getOrder().getOrderStatus());
        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(
                paymentOutboxMessage.get(),
                orderPaidEvent.getOrder().getOrderStatus(),
                sagaStatus));

        approvalOutboxHelper.saveApprovalOutboxMessage(
                orderDataMapper.orderPaidEventToOrderApprovalEventPayload(orderPaidEvent),
                orderPaidEvent.getOrder().getOrderStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(paymentResponse.getSagaId()));

        log.info(ORDER_PAID_FOR_ID.build(orderPaidEvent.getOrder().getId().getValue()));
    }

    private OrderPaidEvent completePaymentForOrder(PaymentResponse paymentResponse) {
        log.info(ORDER_COMPLETING_PAYMENT_FOR_ID.build(paymentResponse.getOrderId()));
        Order order = sagaHelper.findOrder(paymentResponse.getOrderId());
        OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order);
        sagaHelper.saveOrder(order);
        return orderPaidEvent;
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(
            OrderPaymentOutboxMessage paymentOutboxMessage,
            OrderStatus orderStatus,
            SagaStatus sagaStatus) {

        paymentOutboxMessage.setProcessedAt(getZonedDateTimeNow());
        paymentOutboxMessage.setOrderStatus(orderStatus);
        paymentOutboxMessage.setSagaStatus(sagaStatus);
        return paymentOutboxMessage;
    }

    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(
            String sagaId,
            OrderStatus orderStatus,
            SagaStatus sagaStatus) {

        Optional<OrderApprovalOutboxMessage> approvalOutboxMessageResponse =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(sagaId),
                        SagaStatus.COMPENSATING);

        if(approvalOutboxMessageResponse.isEmpty()) {
            throw new OrderDomainException(OUTBOX_MESSAGE_COULD_NOT_BE_FOUND.build(
                    OrderApprovalOutboxMessage.class.getSimpleName(),
                    SagaStatus.COMPENSATING.name()));
        }

        OrderApprovalOutboxMessage orderApprovalOutboxMessage = approvalOutboxMessageResponse.get();
        orderApprovalOutboxMessage.setProcessedAt(getZonedDateTimeNow());
        orderApprovalOutboxMessage.setOrderStatus(orderStatus);
        orderApprovalOutboxMessage.setSagaStatus(sagaStatus);
        return orderApprovalOutboxMessage;
    }

    @Override
    @Transactional
    public void rollback(PaymentResponse paymentResponse) {

        Optional<OrderPaymentOutboxMessage> paymentOutboxMessage =
                paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(paymentResponse.getSagaId()),
                        getSagaStatusFromPaymentStatus(paymentResponse.getPaymentStatus()));

        if (paymentOutboxMessage.isEmpty()) {
            log.info(OUTBOX_MESSAGE_SAGA_ID_ALREADY_ROLLED_BACK.build(paymentResponse.getSagaId()));
            return;
        }

        Order order = rollbackPaymentForOrder(paymentResponse);
        SagaStatus sagaStatus = getSagaStatusFromOrderStatus(order.getOrderStatus());

        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(
                paymentOutboxMessage.get(),
                order.getOrderStatus(),
                sagaStatus));

        if (PaymentStatus.CANCELLED == paymentResponse.getPaymentStatus()) {
            approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(
                    paymentResponse.getSagaId(),
                    order.getOrderStatus(),
                    sagaStatus));
        }

        log.info(ORDER_ID_CANCELED_ID.build(order.getId().getValue()));
    }

    private Order rollbackPaymentForOrder(PaymentResponse paymentResponse) {
        log.info(ORDER_ID_CANCELLING.build(paymentResponse.getOrderId()));
        Order order = sagaHelper.findOrder(paymentResponse.getOrderId());
        orderDomainService.cancelOrder(order,
                paymentResponse.getFailureMessages());
        sagaHelper.saveOrder(order);

        return order;
    }
}
