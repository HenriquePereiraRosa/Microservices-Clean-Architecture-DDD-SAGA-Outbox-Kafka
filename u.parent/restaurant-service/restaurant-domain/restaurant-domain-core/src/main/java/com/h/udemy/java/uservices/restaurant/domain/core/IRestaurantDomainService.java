package com.h.udemy.java.uservices.restaurant.domain.core;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.restaurant.domain.core.entity.Restaurant;
import com.h.udemy.java.uservices.restaurant.domain.core.event.OrderApprovalEvent;
import com.h.udemy.java.uservices.restaurant.domain.core.event.OrderApprovedEvent;
import com.h.udemy.java.uservices.restaurant.domain.core.event.OrderRejectedEvent;

import java.util.List;

public interface IRestaurantDomainService {

    OrderApprovalEvent validateOrder(Restaurant restaurant,
                                     List<String> failureMessages,
                                     IDomainEventPublisher<OrderApprovedEvent> orderApprovedPublisher,
                                     IDomainEventPublisher<OrderRejectedEvent> orderRejectedPublisher);
}
