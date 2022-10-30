package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
import com.h.udemy.java.uservices.order.service.domain.mapper.OrderDataMapper;
import com.h.udemy.java.uservices.order.service.domain.messages.I18n;
import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment.IOrderCreatedPaymentRequestMessagePublisher;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.ICustomerRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IOrderRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IRestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final IOrderDomainService IOrderDomainService;
    private final OrderCreateHelper orderCreateHelper;
    private final IOrderRepository IOrderRepository;
    private final ICustomerRepository ICustomerRepository;
    private final IRestaurantRepository IRestaurantRepository;
    private final OrderDataMapper orderDataMapper;

    private final IOrderCreatedPaymentRequestMessagePublisher IOrderCreatedPaymentRequestMessagePublisher;

    public OrderCreateCommandHandler(IOrderDomainService IOrderDomainService,
                                     OrderCreateHelper orderCreateHelper,
                                     IOrderRepository IOrderRepository,
                                     ICustomerRepository ICustomerRepository,
                                     IRestaurantRepository IRestaurantRepository,
                                     OrderDataMapper orderDataMapper,
                                     IOrderCreatedPaymentRequestMessagePublisher IOrderCreatedPaymentRequestMessagePublisher) {
        this.IOrderDomainService = IOrderDomainService;
        this.orderCreateHelper = orderCreateHelper;
        this.IOrderRepository = IOrderRepository;
        this.ICustomerRepository = ICustomerRepository;
        this.IRestaurantRepository = IRestaurantRepository;
        this.orderDataMapper = orderDataMapper;
        this.IOrderCreatedPaymentRequestMessagePublisher = IOrderCreatedPaymentRequestMessagePublisher;
    }


    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper
                .persistOrder(createOrderCommand);

        IOrderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);

        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(),
                I18n.ORDER_CREATED_SUCCESSFULLY.getMsg());

    }
}
