package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCancelledEvent;
import com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.restaurantApproval.IRestaurantApprovalMessageListener;
import com.h.udemy.java.uservices.order.service.domain.saga.OrderApprovalSaga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_APPROVED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_PUBLISHING_ORDER_CANCELLED_EVENT;
import static com.h.udemy.java.uservices.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;


@Slf4j
@Validated
@Service
public class RestaurantApprovalResponseMessageListener implements IRestaurantApprovalMessageListener {

    private final OrderApprovalSaga orderApprovalSaga;

    public RestaurantApprovalResponseMessageListener(OrderApprovalSaga orderApprovalSaga) {
        this.orderApprovalSaga = orderApprovalSaga;
    }

    @Override
    public void orderApproval(RestaurantApprovalResponse restaurantApprovalResponse) {
        orderApprovalSaga.process(restaurantApprovalResponse);
        log.info(ORDER_ID_APPROVED.build(restaurantApprovalResponse.getOrderId()));
    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {
        OrderCancelledEvent domainEvent = orderApprovalSaga.rollback(restaurantApprovalResponse);

        log.info(ORDER_ID_PUBLISHING_ORDER_CANCELLED_EVENT.build(restaurantApprovalResponse.getOrderId(),
                        String.join(FAILURE_MESSAGE_DELIMITER, restaurantApprovalResponse.getFailureMessages())));

        domainEvent.fire();
    }
}
