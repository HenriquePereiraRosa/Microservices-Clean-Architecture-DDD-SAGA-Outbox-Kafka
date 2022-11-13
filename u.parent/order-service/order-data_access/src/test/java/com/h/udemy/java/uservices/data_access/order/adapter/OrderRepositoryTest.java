package com.h.udemy.java.uservices.data_access.order.adapter;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.ProductId;
import com.h.udemy.java.uservices.domain.valueobject.RestaurantId;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.OrderItem;
import com.h.udemy.java.uservices.order.service.domain.entity.Product;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IOrderRepository;
import com.h.udemy.java.uservices.order.service.domain.valueobject.OrderItemId;
import com.h.udemy.java.uservices.order.service.domain.valueobject.StreetAddress;
import com.h.udemy.java.uservices.order.service.domain.valueobject.TrackingId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {

    @Autowired
    IOrderRepository orderRepository;

    private final TrackingId TRACKING_ID = new TrackingId(UUID.randomUUID());

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
                .customerId(new CustomerId(UUID.randomUUID()))
                .restaurantId(new RestaurantId(UUID.randomUUID()))
                .deliveryAddress(address)
                .price(new Money(new BigDecimal(54.95)))
                .items(Arrays.asList(item))
                .trackingId(TRACKING_ID)
                .build();
    }
}