package com.h.udemy.java.uservices.order.service.dataaccess.test.util.factory;

import com.h.udemy.java.uservices.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.h.udemy.java.uservices.order.service.dataaccess.order.entity.OrderEntity;
import com.h.udemy.java.uservices.order.service.dataaccess.order.entity.OrderItemEntity;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.domain.valueobject.OrderStatus;
import com.h.udemy.java.uservices.domain.valueobject.ProductId;
import com.h.udemy.java.uservices.domain.valueobject.RestaurantId;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.OrderItem;
import com.h.udemy.java.uservices.order.service.domain.entity.Product;
import com.h.udemy.java.uservices.order.service.domain.valueobject.OrderItemId;
import com.h.udemy.java.uservices.order.service.domain.valueobject.StreetAddress;
import com.h.udemy.java.uservices.order.service.domain.valueobject.TrackingId;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

public class OrderFactory {

    public static final OrderId ORDER_ID = new OrderId(UUID.randomUUID());
    public static final UUID ORDER_ENTITY_ID = ORDER_ID.getValue();
    public static final TrackingId TRACKING_ID = new TrackingId(UUID.randomUUID());
    public static final UUID STREET_ADDRESS_ID = UUID.randomUUID();
    public static final CustomerId CUSTOMER_ID = new CustomerId(UUID.randomUUID());
    public static final RestaurantId RESTAURANT_ID = new RestaurantId(UUID.randomUUID());
    public static final ProductId PRODUCT_ID = new ProductId(UUID.randomUUID());


    static public Order createOrder() {

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


    static public OrderEntity createOrderEntity() {

        OrderAddressEntity orderAddressEntity = new OrderAddressEntity(STREET_ADDRESS_ID,
                null,
                "sweet street",
                "01234-99",
                "Tokio");

        OrderItemEntity item = OrderItemEntity.builder()
                .id(112L)
                .productId(PRODUCT_ID.getValue())
                .price(new BigDecimal(10.99))
                .quantity(5)
                .build();

        OrderEntity orderEntity = OrderEntity.builder()
                .id(ORDER_ENTITY_ID)
                .customerId(CUSTOMER_ID.getValue())
                .restaurantId(RESTAURANT_ID.getValue())
                .orderStatus(OrderStatus.PENDING)
                .price(new BigDecimal(54.95))
                .items(Arrays.asList(item))
                .trackingId(TRACKING_ID.getValue())
                .address(orderAddressEntity)
                .failureMessages("")
                .build();

        orderEntity.getAddress().setOrder(orderEntity);

        return orderEntity;
    }
}
