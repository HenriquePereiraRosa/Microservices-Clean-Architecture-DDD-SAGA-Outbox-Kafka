package com.h.udemy.java.uservices.factory;

import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.dto.create.OrderAddressDTO;
import com.h.udemy.java.uservices.order.service.domain.dto.create.OrderItemDTO;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.OrderItem;
import com.h.udemy.java.uservices.order.service.domain.entity.Product;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
import com.h.udemy.java.uservices.order.service.domain.valueobject.OrderItemId;
import com.h.udemy.java.uservices.order.service.domain.valueobject.StreetAddress;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static com.h.udemy.java.uservices.domain.Const.ZONED_UTC;
import static com.h.udemy.java.uservices.test.util.ConstTestUtils.*;

public class OrderFactory {

    static public CreateOrderCommand createCreateOrderCommand() {

        OrderAddressDTO orderAddressDTO = new OrderAddressDTO("sweet street",
                "01234-99",
                "Tokio");

        OrderItemDTO item = OrderItemDTO.builder()
                .productId(PRODUCT_ID.getValue())
                .price(new BigDecimal(10.99))
                .quantity(5)
                .build();

        return CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID.getValue())
                .restaurantId(RESTAURANT_ID.getValue())
                .address(orderAddressDTO)
                .price(new BigDecimal(54.95))
                .items(Arrays.asList(item))
                .build();
    }

    static public Order createOrderSaved() {

        StreetAddress address = new StreetAddress(STREET_ADDRESS_ID,
                "sweet street",
                "01234-99",
                "Tokio");

        OrderItem item = OrderItem.builder()
                .orderItemId(new OrderItemId(112L))
                .product(new Product(PRODUCT_ID,
                        "product name",
                        new Money(new BigDecimal(10.99))))
                .price(new Money(new BigDecimal(10.99)))
                .quantity(5)
                .build();

        return Order.builder()
                .orderId(ORDER_ID)
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .deliveryAddress(address)
                .price(new Money(new BigDecimal(54.95)))
                .items(Arrays.asList(item))
                .trackingId(TRACKING_ID)
                .build();
    }

    static public OrderCreatedEvent createOrderCreatedEvent(Order order) {
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZONED_UTC));
    }
}
