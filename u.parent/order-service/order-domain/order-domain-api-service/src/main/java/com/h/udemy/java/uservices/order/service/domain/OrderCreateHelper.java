package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.entity.Customer;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
import com.h.udemy.java.uservices.order.service.domain.exception.CustomerNotFoundException;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderCouldNotBeSavedException;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainException;
import com.h.udemy.java.uservices.order.service.domain.messages.I18n;
import com.h.udemy.java.uservices.order.service.domain.mapper.OrderDataMapper;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.ICustomerRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IOrderRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IRestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateHelper {

    private final IOrderDomainService iOrderDomainService;
    private final IOrderRepository orderRepository;
    private final ICustomerRepository iCustomerRepository;
    private final IRestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateHelper(IOrderDomainService orderDomainService,
                             IOrderRepository orderRepository,
                             ICustomerRepository iCustomerRepository,
                             IRestaurantRepository restaurantRepository,
                             OrderDataMapper orderDataMapper) {
        this.iOrderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.iCustomerRepository = iCustomerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }
    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.customerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = iOrderDomainService
                .validateAndInitiateOrder(order, restaurant);
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
                .findRestaurantInformation(restaurant);
        if (lRestaurant.isEmpty()) {
            final String msg = I18n.ERR_RESTAURANT_NOT_FOUND.getMsg() +
                    createOrderCommand.restaurantId().toString();
            log.warn(msg);
            throw new OrderDomainException(msg);
        }
        return lRestaurant.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = iCustomerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            final String msg = I18n.ERR_CUSTOMER_NOT_FOUND.getMsg() + customerId;
            log.warn(msg);
            throw new CustomerNotFoundException(customerId);
        }
    }

    private Order insertOrder(Order order) {
        Order orderCreated = orderRepository.insertOrder(order);
        if (orderCreated == null) {
            final String msg = I18n.ERR_ORDER_COULD_NOT_BE_SAVED.getMsg() +
                    order.getId().getValue();
            log.warn(msg);
            throw new OrderCouldNotBeSavedException(order.getId().getValue());
        }
        return orderCreated;
    }
}
