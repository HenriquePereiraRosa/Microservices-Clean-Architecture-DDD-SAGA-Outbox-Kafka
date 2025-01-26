package com.h.udemy.java.uservices.order.service.domain.saga;

import static com.h.udemy.java.uservices.domain.Constants.ZONED_DATE_TIME;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_COMPLETING_PAYMENT_FOR_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELED_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELLING;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_PAID_FOR_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_MESSAGE_ALREADY_PROCESSED;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.h.udemy.java.uservices.domain.valueobject.OrderStatus;
import com.h.udemy.java.uservices.order.service.domain.IOrderDomainService;
import com.h.udemy.java.uservices.order.service.domain.dto.message.PaymentResponse;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;
import com.h.udemy.java.uservices.order.service.domain.mapper.OrderDataMapper;
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

    private final IOrderDomainService orderDomainService;
    private final OrderSagaHelper sagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final OrderSagaHelper orderSagaHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final OrderDataMapper orderDataMapper;

    public OrderPaymentSaga(
            IOrderDomainService orderDomainService,
            OrderSagaHelper sagaHelper,
            PaymentOutboxHelper paymentOutboxHelper,
            OrderSagaHelper orderSagaHelper,
            ApprovalOutboxHelper approvalOutboxHelper,
            OrderDataMapper orderDataMapper) {

        this.orderDomainService = orderDomainService;
        this.sagaHelper = sagaHelper;
        this.paymentOutboxHelper = paymentOutboxHelper;
        this.orderSagaHelper = orderSagaHelper;
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

        log.info(ORDER_COMPLETING_PAYMENT_FOR_ID.build(paymentResponse.getOrderId()));
        Order order = sagaHelper.findOrder(paymentResponse.getOrderId());
        OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order);
        sagaHelper.saveOrder(order);

        SagaStatus sagaStatus = orderSagaHelper
                .orderStatusToSagaStatus(orderPaidEvent.getOrder().getOrderStatus());
        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(
                paymentOutboxMessage.get(),
                orderPaidEvent.getOrder().getOrderStatus(),
                sagaStatus));

        approvalOutboxHelper.saveApprovalOutboxMessage(
                orderDataMapper.orderPaidEventToOrderApprovalEventPayload(orderPaidEvent),
                orderPaidEvent.getOrder().getOrderStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(paymentResponse.getOrderId()));

        log.info(ORDER_PAID_FOR_ID.build(paymentResponse.getOrderId()));
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(
            OrderPaymentOutboxMessage paymentOutboxMessage,
            OrderStatus orderStatus,
            SagaStatus sagaStatus) {

        paymentOutboxMessage.setProcessedAt(ZONED_DATE_TIME);
        paymentOutboxMessage.setOrderStatus(orderStatus);
        paymentOutboxMessage.setSagaStatus(sagaStatus);

        return paymentOutboxMessage;
    }

    @Override
    @Transactional
    public void rollback(PaymentResponse paymentResponse) {
        log.info(ORDER_ID_CANCELLING.build(paymentResponse.getOrderId()));
        Order order = sagaHelper.findOrder(paymentResponse.getOrderId());
        orderDomainService.cancelOrder(order,
                paymentResponse.getFailureMessages());
        sagaHelper.saveOrder(order);
        log.info(ORDER_ID_CANCELED_ID.build(paymentResponse.getOrderId()));
    }
}
