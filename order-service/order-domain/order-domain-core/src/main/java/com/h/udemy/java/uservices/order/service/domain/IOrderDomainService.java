package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCancelledEvent;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;

import java.util.List;

public interface IOrderDomainService {

    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant, IDomainEventPublisher<OrderCreatedEvent> createdEventPublisher);

    OrderPaidEvent payOrder(Order order,
                            IDomainEventPublisher<OrderPaidEvent> orderPaidEventDomainEventPublisher);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages, IDomainEventPublisher<OrderCancelledEvent> cancelledEventPublisher);

    void cancelOrder(Order order, List<String> failureMessages);
}
