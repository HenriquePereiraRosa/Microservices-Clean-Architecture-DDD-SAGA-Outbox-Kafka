package com.h.udemy.java.uservices.order.service.application.test.util.factory;

import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.order.service.domain.entity.Product;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;

import java.math.BigDecimal;
import java.util.List;

import static com.h.udemy.java.uservices.order.service.application.test.util.OrderTestConstants.PRODUCT_ID;
import static com.h.udemy.java.uservices.order.service.application.test.util.OrderTestConstants.RESTAURANT_ID;

public class RestaurantFactory {

    static public Restaurant createRestaurant() {

        Product product = new Product(PRODUCT_ID,
                        "product name",
                        new Money(new BigDecimal("10.99")));

        return Restaurant.builder()
                .restaurantId(RESTAURANT_ID)
                .products(List.of(product))
                .active(true)
                .build();
    }
}
