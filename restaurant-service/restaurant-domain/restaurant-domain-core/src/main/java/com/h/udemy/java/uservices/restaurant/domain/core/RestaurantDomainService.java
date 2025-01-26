package com.h.udemy.java.uservices.restaurant.domain.core;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.domain.valueobject.OrderApprovalStatus;
import com.h.udemy.java.uservices.restaurant.domain.core.entity.Restaurant;
import com.h.udemy.java.uservices.restaurant.domain.core.event.OrderApprovalEvent;
import com.h.udemy.java.uservices.restaurant.domain.core.event.OrderApprovedEvent;
import com.h.udemy.java.uservices.restaurant.domain.core.event.OrderRejectedEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.h.udemy.java.uservices.domain.Constants.ZONED_DATE_TIME;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.RESTAURANT_APPROVED_ORDER_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.RESTAURANT_REJECTED_ORDER_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.RESTAURANT_VALIDATING_ORDER_ID;

@Slf4j
public class RestaurantDomainService implements IRestaurantDomainService {


    @Override
    public OrderApprovalEvent validateOrder(Restaurant restaurant,
                                            List<String> failureMessages,
                                            DomainEventPublisher<OrderApprovedEvent> orderApprovedPublisher,
                                            DomainEventPublisher<OrderRejectedEvent> orderRejectedPublisher) {


        restaurant.validateOrder(failureMessages);
        log.info(RESTAURANT_VALIDATING_ORDER_ID
                .build(restaurant.getOrderDetail().getId().getValue()));


        if(failureMessages.isEmpty()) {
            log.info(RESTAURANT_APPROVED_ORDER_ID
                    .build(restaurant.getOrderDetail().getId().getValue()));

            restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(restaurant.getOrderApproval(),
                    restaurant.getId(),
                    failureMessages,
                    ZONED_DATE_TIME,
                    orderApprovedPublisher);
        }


        log.info(RESTAURANT_REJECTED_ORDER_ID
                .build(restaurant.getOrderDetail().getId().getValue()));

        restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
        return new OrderRejectedEvent(restaurant.getOrderApproval(),
                restaurant.getId(),
                failureMessages,
                ZONED_DATE_TIME,
                orderRejectedPublisher);
    }
}
