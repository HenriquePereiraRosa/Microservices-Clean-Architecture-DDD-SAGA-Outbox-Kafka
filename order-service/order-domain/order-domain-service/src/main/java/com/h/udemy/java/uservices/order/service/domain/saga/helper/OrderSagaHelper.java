package com.h.udemy.java.uservices.order.service.domain.saga.helper;

import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderNotFoundException;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_ORDER_NOT_FOUND;

@Slf4j
@Component
public class OrderSagaHelper {
    private final OrderRepository orderRepository;

    public OrderSagaHelper(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findOrder(String pOrderId) {
        Optional<Order> orderOp = orderRepository.findById(new OrderId(UUID.fromString(pOrderId)));

        if(orderOp.isEmpty()) {
            final String message = ERR_ORDER_NOT_FOUND.build(pOrderId);
            log.error(message);
            throw new OrderNotFoundException(message);
        }

        return orderOp.get();
    }

    public void saveOrder(Order pOrder) {
        orderRepository.save(pOrder);
    }
}
