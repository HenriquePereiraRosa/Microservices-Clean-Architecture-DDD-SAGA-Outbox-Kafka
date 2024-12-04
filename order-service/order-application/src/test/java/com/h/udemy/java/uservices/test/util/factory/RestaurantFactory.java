package com.h.udemy.java.uservices.test.util.factory;

import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.order.service.domain.entity.Product;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.h.udemy.java.uservices.test.util.ConstTestUtils.PRODUCT_ID;
import static com.h.udemy.java.uservices.test.util.ConstTestUtils.RESTAURANT_ID;

public class RestaurantFactory {

    static public Restaurant createRestaurant() {

        Product product = new Product(PRODUCT_ID,
                        "product name",
                        new Money(new BigDecimal(10.99)));

        return Restaurant.builder()
                .restaurantId(RESTAURANT_ID)
                .products(Arrays.asList(product))
                .active(true)
                .build();
    }
}
