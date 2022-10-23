package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCancelledEvent;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainException;
import com.h.udemy.java.uservices.order.service.domain.exception.msg.I18n;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainService implements IOrderDomainService {
    @Override
    public OrderCreatedEvent validateAndInitateOrder(Order order, Restaurant restaurant) {
        validateResaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initialeOrder();
        log.info(I18n.ORDER_ID_INITIATED.getMsg(), order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        return null;
    }

    @Override
    public void approveOrder(Order order) {

    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        return null;
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {

    }

    private void validateResaurant(Restaurant restaurant) {
        if(!restaurant.isActive()) {
            throw new OrderDomainException(I18n.ERR_RESTAURANT_ID_NOT_ACTIVE.getMsg()
                    + restaurant.getId());
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
    }
}
