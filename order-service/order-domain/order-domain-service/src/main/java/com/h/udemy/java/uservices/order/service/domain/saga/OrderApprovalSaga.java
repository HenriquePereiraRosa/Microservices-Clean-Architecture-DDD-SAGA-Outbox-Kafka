package com.h.udemy.java.uservices.order.service.domain.saga;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_APPROVED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_APPROVING;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELED_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELLING;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.h.udemy.java.uservices.order.service.domain.IOrderDomainService;
import com.h.udemy.java.uservices.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCancelledEvent;
import com.h.udemy.java.uservices.order.service.domain.saga.helper.OrderSagaHelper;
import com.h.udemy.java.uservices.saga.SagaStep;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse> {

    private final IOrderDomainService orderDomainService;
    private final OrderSagaHelper sagaHelper;

    public OrderApprovalSaga(IOrderDomainService orderDomainService,
            OrderSagaHelper sagaHelper) {
        this.orderDomainService = orderDomainService;
        this.sagaHelper = sagaHelper;
    }

    @Override
    @Transactional
    public void process(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info(ORDER_ID_APPROVING.build(restaurantApprovalResponse.getOrderId()));
        Order order = sagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        orderDomainService.approveOrder(order);
        sagaHelper.saveOrder(order);
        log.info(ORDER_ID_APPROVED.build(restaurantApprovalResponse.getOrderId()));
    }

    @Override
    @Transactional
    public void rollback(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info(ORDER_ID_CANCELLING.build(restaurantApprovalResponse.getOrderId()));
        Order order = sagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        orderDomainService.cancelOrderPayment(
                order,
                restaurantApprovalResponse.getFailureMessages());

        sagaHelper.saveOrder(order);
        log.info(ORDER_ID_CANCELED_ID.build(restaurantApprovalResponse.getOrderId()));
    }
}
