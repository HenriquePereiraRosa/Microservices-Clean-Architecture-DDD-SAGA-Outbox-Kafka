package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.entity.Customer;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
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
    private final IOrderRepository iOrderRepository;
    private final ICustomerRepository iCustomerRepository;
    private final IRestaurantRepository iRestaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateHelper(OrderDomainService orderDomainServiceImpl,
                             IOrderRepository iOrderRepository,
                             ICustomerRepository iCustomerRepository,
                             IRestaurantRepository iRestaurantRepository,
                             OrderDataMapper orderDataMapper) {
        this.iOrderDomainService = orderDomainServiceImpl;
        this.iOrderRepository = iOrderRepository;
        this.iCustomerRepository = iCustomerRepository;
        this.iRestaurantRepository = iRestaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }
    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
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
        Optional<Restaurant> lRestaurant = iRestaurantRepository
                .findInformation(restaurant);
        if (lRestaurant.isEmpty()) {
            final String msg = I18n.ERR_RESTAURANT_NOT_FOUND.getMsg() +
                    createOrderCommand.getRestaurantId().toString();
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
            throw new OrderDomainException(msg);
        }
    }

    private Order insertOrder(Order order) {
        Order orderCreated = iOrderRepository.insertOrder(order);
        if (orderCreated == null) {
            final String msg = I18n.ERR_ORDER_COULD_NOT_BE_SAVED.getMsg() +
                    order.getId().getValue();
            log.warn(msg);
            throw new OrderDomainException(msg);
        }
        return orderCreated;
    }
}
