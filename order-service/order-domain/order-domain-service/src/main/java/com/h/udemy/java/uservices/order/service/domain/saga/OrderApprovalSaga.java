package com.h.udemy.java.uservices.order.service.domain.saga;

import com.h.udemy.java.uservices.domain.event.EmptyEvent;
import com.h.udemy.java.uservices.order.service.domain.IOrderDomainService;
import com.h.udemy.java.uservices.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCancelledEvent;
import com.h.udemy.java.uservices.order.service.domain.saga.helper.OrderSagaHelper;
import com.h.udemy.java.uservices.saga.SagaStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_APPROVED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_APPROVING;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELED_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELLING;

@Slf4j
@Component
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse, EmptyEvent, OrderCancelledEvent> {

    private final IOrderDomainService orderDomainService;
    private final OrderSagaHelper sagaHelper;
    private final OrderCancelledPaymentRequestRequestMessagePublisher messagePublisher;

    public OrderApprovalSaga(IOrderDomainService orderDomainService,
                             OrderSagaHelper sagaHelper,
                             OrderCancelledPaymentRequestRequestMessagePublisher messagePublisher) {
        this.orderDomainService = orderDomainService;
        this.sagaHelper = sagaHelper;
        this.messagePublisher = messagePublisher;
    }

    @Override
    @Transactional
    public EmptyEvent process(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info(ORDER_ID_APPROVING.build(restaurantApprovalResponse.getOrderId()));
        Order order = sagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        orderDomainService.approveOrder(order);
        sagaHelper.saveOrder(order);
        log.info(ORDER_ID_APPROVED.build(restaurantApprovalResponse.getOrderId()));
        return EmptyEvent.INSTANCE;
    }

    @Override
    @Transactional
    public OrderCancelledEvent rollback(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info(ORDER_ID_CANCELLING.build(restaurantApprovalResponse.getOrderId()));
        Order order = sagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        OrderCancelledEvent domainEvent = orderDomainService.cancelOrderPayment(order,
                restaurantApprovalResponse.getFailureMessages(),
                messagePublisher);
        sagaHelper.saveOrder(order);
        log.info(ORDER_ID_CANCELED_ID.build(restaurantApprovalResponse.getOrderId()));
        return domainEvent;
    }
}
