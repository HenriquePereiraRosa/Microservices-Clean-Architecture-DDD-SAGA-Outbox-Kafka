package com.h.udemy.java.uservices.dataaccess.order.mapper;

import com.h.udemy.java.uservices.dataaccess.ApiEnvTestConfig;
import com.h.udemy.java.uservices.dataaccess.order.entity.OrderEntity;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import static com.h.udemy.java.uservices.dataaccess.test.util.builder.OrderBuilder.createOrder;
import static com.h.udemy.java.uservices.dataaccess.test.util.builder.OrderBuilder.createOrderEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderDataAccessMapperTest extends ApiEnvTestConfig {

    private final Order order = createOrder();
    private final OrderEntity orderEntity = createOrderEntity();

    @Autowired
    OrderDataAccessMapper orderMapper;

    @BeforeAll
    void setup() {

    }

    @Test
    void orderToOrderEntity_ok() {
        OrderEntity orderEntity1 = orderMapper.orderToOrderEntity(order);

        assertEquals(order.getOrderStatus(), orderEntity1.getOrderStatus());
        assertEquals(order.getPrice().getAmount(), orderEntity1.getPrice());
        assertEquals(order.getDeliveryAddress().street(), orderEntity1.getAddress().getStreet());

    }

    @Test
    void orderEntityToOrder_ok() {
        Order order1 = orderMapper.orderEntityToOrder(orderEntity);

        assertEquals(orderEntity.getOrderStatus(), order1.getOrderStatus());
        assertTrue(order1.getPrice().compareTo(orderEntity.getPrice()));
        assertEquals(orderEntity.getAddress().getStreet(), order1.getDeliveryAddress().street());
    }
}