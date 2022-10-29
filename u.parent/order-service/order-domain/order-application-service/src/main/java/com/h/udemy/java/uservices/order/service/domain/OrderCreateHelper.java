package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.entity.Customer;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainException;
import com.h.udemy.java.uservices.order.service.domain.exception.msg.I18n;
import com.h.udemy.java.uservices.order.service.domain.mapper.OrderDataMapper;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.CustomerRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.OrderRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateHelper {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateHelper(OrderDomainService orderDomainService,
                             OrderRepository orderRepository,
                             CustomerRepository customerRepository,
                             RestaurantRepository restaurantRepository,
                             OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }
    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService
                .validateAndInitateOrder(order, restaurant);
        insertOrder(order);

        final String msg = I18n.ORDER_ID_CREATED.getMsg() +
                orderCreatedEvent.getOrder().getId().getValue();
        log.warn(msg);

        return orderCreatedEvent;
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper
                .createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> lRestaurant = restaurantRepository
                .findInformation(restaurant);
        if (lRestaurant.isEmpty()) {
            final String msg = I18n.ERR_RESTAURANT_NOT_FOUND.getMsg() +
                    createOrderCommand.getRestaurantId();
            log.warn(msg);
            throw new OrderDomainException(msg);
        }
        return lRestaurant.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn(I18n.ERR_RESTAURANT_ID_NOT_ACTIVE.getMsg(), customerId);
            final String msg = I18n.ERR_RESTAURANT_ID_NOT_ACTIVE.getMsg() + customerId;
            log.warn(msg);
            throw new OrderDomainException(msg);
        }
    }

    private Order insertOrder(Order order) {
        Order orderCreated = orderRepository.insertOrder(order);
        if (orderCreated == null) {
            final String msg = I18n.ERR_ORDER_COULD_NOT_BE_SAVED.getMsg() +
                    order.getId().getValue();
            log.warn(msg);
            throw new OrderDomainException(msg);
        }
        return orderCreated;
    }
}
