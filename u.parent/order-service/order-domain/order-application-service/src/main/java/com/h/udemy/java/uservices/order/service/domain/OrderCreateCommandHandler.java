package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
import com.h.udemy.java.uservices.order.service.domain.mapper.OrderDataMapper;
import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.CustomerRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.OrderRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderCreateHelper orderCreateHelper;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    public OrderCreateCommandHandler(OrderDomainService orderDomainService,
                                     OrderCreateHelper orderCreateHelper,
                                     OrderRepository orderRepository,
                                     CustomerRepository customerRepository,
                                     RestaurantRepository restaurantRepository,
                                     OrderDataMapper orderDataMapper,
                                     OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher) {
        this.orderDomainService = orderDomainService;
        this.orderCreateHelper = orderCreateHelper;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
    }


    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper
                .persistOrder(createOrderCommand);

        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);

        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder());

    }
}
