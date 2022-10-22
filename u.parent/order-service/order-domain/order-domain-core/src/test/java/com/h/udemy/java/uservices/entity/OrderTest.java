package com.h.udemy.java.uservices.entity;

import com.h.udemy.java.uservices.domain.valueobject.*;
import com.h.udemy.java.uservices.exception.OrderDomainInitialStateException;
import com.h.udemy.java.uservices.exception.msg.I18n;
import com.h.udemy.java.uservices.util.validation.Valid;
import com.h.udemy.java.uservices.valueobject.OrderItemId;
import com.h.udemy.java.uservices.valueobject.StreetAddress;
import com.h.udemy.java.uservices.valueobject.TrackingId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderTest extends ApiEnvTest {

    @Test
    void getId() {
        Order order = this.getOne();
        assertTrue(Valid.isUUID(order.getId().getValue()));
    }

    @Test
    void setId() {
        Order order = this.getOne();
        assertTrue(Valid.isUUID(order.getId().getValue()));
    }

    @Test
    void testEquals() {
        Order order1 = this.getOne();
        Order order2 = this.getOne();
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
        order.initialeOrder();
        assertEquals(OrderStatus.PENDING, order.getOrderStatus());
    }

    @Test
    void validateOrder_nok_OrderDomainInitialStateException() {
        Order order = this.getOne();

        Throwable exceptionThatWasThrown =
                assertThrows(OrderDomainInitialStateException.class, () -> {
                    order.validateOrder();
                });

        assertEquals(exceptionThatWasThrown.getMessage(),
                I18n.ERR_ORDER_NOT_CORRECT_INIT_STATE.getMsg());
    }

    @Test
    void validateOrder_ok() {
        Order order = this.getOne();
        order.initialeOrder();
        order.validateOrder();
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
                        new Money(new BigDecimal(10.99))))
                .price(new Money(new BigDecimal(10.99)))
                .quantity(5)
                .build();

        return Order.builder()
                .orderId(new OrderId(UUID.randomUUID()))
                .customerId(new CustomerId(UUID.randomUUID()))
                .restaurantId(new RestaurantId(UUID.randomUUID()))
                .deliveryAddress(address)
                .price(new Money(new BigDecimal(54.95)))
                .items(Arrays.asList(item))
                .trackingId(new TrackingId(UUID.randomUUID()))
                .orderStatus(OrderStatus.APPROVED)
                .build();
    }
}