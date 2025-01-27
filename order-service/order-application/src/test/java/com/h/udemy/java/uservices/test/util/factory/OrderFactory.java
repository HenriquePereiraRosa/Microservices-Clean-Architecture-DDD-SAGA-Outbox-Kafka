package com.h.udemy.java.uservices.test.util.factory;

import static com.h.udemy.java.uservices.domain.Constants.getZonedDateTimeNow;
import static com.h.udemy.java.uservices.test.util.ConstTestUtils.CUSTOMER_ID;
import static com.h.udemy.java.uservices.test.util.ConstTestUtils.ORDER_ID;
import static com.h.udemy.java.uservices.test.util.ConstTestUtils.PRODUCT_ID;
import static com.h.udemy.java.uservices.test.util.ConstTestUtils.RESTAURANT_ID;
import static com.h.udemy.java.uservices.test.util.ConstTestUtils.STREET_ADDRESS_ID;
import static com.h.udemy.java.uservices.test.util.ConstTestUtils.TRACKING_ID;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

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

public class OrderFactory {

    public static CreateOrderCommand createCreateOrderCommand() {

        OrderAddressDTO orderAddressDTO = new OrderAddressDTO("sweet street",
                "01234-99",
                "Tokio");

        OrderItemDTO item = OrderItemDTO.builder()
                .productId(PRODUCT_ID.getValue())
                .price(new BigDecimal("10.99"))
                .quantity(5)
                .build();

        return CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID.getValue())
                .restaurantId(RESTAURANT_ID.getValue())
                .address(orderAddressDTO)
                .price(new BigDecimal("54.95"))
                .items(Collections.singletonList(item))
                .build();
    }

    public static Order createOrderSaved() {

        StreetAddress address = new StreetAddress(STREET_ADDRESS_ID,
                "sweet street",
                "01234-99",
                "Tokio");

        OrderItem item = OrderItem.builder()
                .orderItemId(new OrderItemId(112L))
                .product(new Product(PRODUCT_ID,
                        "product name",
                        new Money(new BigDecimal("10.99"))))
                .price(new Money(new BigDecimal("10.99")))
                .quantity(5)
                .build();

        return Order.builder()
                .orderId(ORDER_ID)
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .deliveryAddress(address)
                .price(new Money(new BigDecimal("54.95")))
                .items(List.of(item))
                .trackingId(TRACKING_ID)
                .build();
    }

    public static OrderCreatedEvent createOrderCreatedEvent(Order order) {
        return new OrderCreatedEvent(order, getZonedDateTimeNow());
    }
}
