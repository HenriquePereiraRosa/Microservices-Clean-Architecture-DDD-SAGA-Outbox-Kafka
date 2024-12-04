package com.h.udemy.java.uservices.order.service.dataaccess.order.adapter;

import com.h.udemy.java.uservices.order.service.dataaccess.ApiEnvTestConfig;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.domain.valueobject.ProductId;
import com.h.udemy.java.uservices.domain.valueobject.RestaurantId;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.OrderItem;
import com.h.udemy.java.uservices.order.service.domain.entity.Product;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IOrderRepository;
import com.h.udemy.java.uservices.order.service.domain.valueobject.OrderItemId;
import com.h.udemy.java.uservices.order.service.domain.valueobject.StreetAddress;
import com.h.udemy.java.uservices.order.service.domain.valueobject.TrackingId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerRepositoryTest extends ApiEnvTestConfig {

    private static final OrderId ORDER_ID = new OrderId(UUID.randomUUID());
    private final TrackingId TRACKING_ID = new TrackingId(UUID.randomUUID());

    @Autowired
    IOrderRepository orderRepository;


    private final Order order = this.getOne();

    @BeforeAll
    public void setup(){

        when(orderRepository.insertOrder(any(Order.class)))
                .thenReturn(order);

        when(orderRepository.findByTrackingId(TRACKING_ID))
                .thenReturn(Optional.of(order));
    }

    @Test
    void insertOrder() {

        Order dummyOrder = this.getOne();

        Order orderDb = orderRepository.insertOrder(dummyOrder);

        assertEquals(dummyOrder.getPrice(), orderDb.getPrice());
    }

    @Test
    void findByTrackingId() {

        Order dummyOrder = this.getOne();

        final Optional<Order> orderDb = orderRepository.findByTrackingId(TRACKING_ID);

        assertNotNull(orderDb.get());
        assertEquals(dummyOrder.getPrice(), orderDb.get().getPrice());
        assertEquals(dummyOrder.getTrackingId(), orderDb.get().getTrackingId());
    }


    private Order getOne() {

        StreetAddress address = new StreetAddress(UUID.randomUUID(),
                "sweet street",
                "01234-99",
                "Tokio");

        OrderItem item = OrderItem.builder()
                .orderItemId(new OrderItemId(112L))
                .product(new Product(new ProductId(UUID.randomUUID()),
                        "product name",
                        new Money(new BigDecimal(10.99))))
                .price(new Money(new BigDecimal(10.99)))
                .quantity(5)
                .build();

        return Order.builder()
                .orderId(ORDER_ID)
                .customerId(new CustomerId(UUID.randomUUID()))
                .restaurantId(new RestaurantId(UUID.randomUUID()))
                .deliveryAddress(address)
                .price(new Money(new BigDecimal(54.95)))
                .items(Arrays.asList(item))
                .trackingId(TRACKING_ID)
                .build();
    }
}