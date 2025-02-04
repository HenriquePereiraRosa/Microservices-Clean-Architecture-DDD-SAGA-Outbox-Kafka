package com.h.udemy.java.uservices.order.service.domain.entity;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.domain.valueobject.OrderStatus;
import com.h.udemy.java.uservices.domain.valueobject.ProductId;
import com.h.udemy.java.uservices.domain.valueobject.RestaurantId;
import com.h.udemy.java.uservices.order.service.domain.ApiEnvTestConfig;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainInitialStateException;
import com.h.udemy.java.uservices.order.service.domain.validation.Valid;
import com.h.udemy.java.uservices.order.service.domain.valueobject.OrderItemId;
import com.h.udemy.java.uservices.order.service.domain.valueobject.StreetAddress;
import com.h.udemy.java.uservices.order.service.domain.valueobject.TrackingId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_ORDER_NOT_CORRECT_INIT_STATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderTest extends ApiEnvTestConfig {

    @Test
    void getId() {
        Order order = this.getOne();
        order.setId(new OrderId(UUID.randomUUID()));
        assertTrue(Valid.isUUID(order.getId().getValue()));
    }

    @Test
    void testEquals() {
        Order order1 = this.getOne();
        order1.setId(new OrderId(UUID.randomUUID()));
        Order order2 = this.getOne();
        order2.setId(new OrderId(UUID.randomUUID()));

        assertNotEquals(order1, order2);
    }

    @Test
    void getCustomerId() {
        Order order = this.getOne();
        assertTrue(Valid.isUUID(order.getCustomerId().getValue()));
    }

    @Test
    void getRestaurantId() {
        Order order = this.getOne();
        assertTrue(Valid.isUUID(order.getRestaurantId().getValue()));
    }

    @Test
    void getTrackingId() {
        Order order = this.getOne();
        assertTrue(Valid.isUUID(order.getTrackingId().getValue()));
    }

    @Test
    void getOrderStatus() {
        Order order = this.getOne();
        order.initializeOrder();
        order.pay();
        order.approve();
        assertEquals(OrderStatus.APPROVED, order.getOrderStatus());
    }

    @Test
    void test_that_failureMessages_is_0_in_initial_stage() {
        Order order = this.getOne();
        assertEquals(0L, order.getFailureMessages().size());
    }

    @Test
    void initializeOrder() {
        Order order = this.getOne();
        order.initializeOrder();
        assertEquals(OrderStatus.PENDING, order.getOrderStatus());
    }

    @Test
    void validateOrder_nok_OrderDomainInitialStateException() {
        Order order = this.getOne();
        order.initializeOrder();
        order.pay();

        Throwable exceptionThatWasThrown =
                assertThrows(OrderDomainInitialStateException.class, order::validateOrder);

        assertEquals(exceptionThatWasThrown.getMessage(),
                ERR_ORDER_NOT_CORRECT_INIT_STATE.get());
    }

    @Test
    void validateOrder_ok() {
        Order order = this.getOne();
        order.validateOrder();
        order.initializeOrder();
    }

    @Test
    void pay() {
    }

    @Test
    void approve() {
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
                        new Money(new BigDecimal("10.99"))))
                .price(new Money(new BigDecimal("10.99")))
                .quantity(5)
                .build();

        return Order.builder()
                .customerId(new CustomerId(UUID.randomUUID()))
                .restaurantId(new RestaurantId(UUID.randomUUID()))
                .deliveryAddress(address)
                .price(new Money(new BigDecimal("54.95")))
                .items(List.of(item))
                .trackingId(new TrackingId(UUID.randomUUID()))
                .build();
    }
}