package com.h.udemy.java.uservices.dataaccess.restaurant.mapper;

import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.ProductId;
import com.h.udemy.java.uservices.domain.valueobject.RestaurantId;
import com.h.udemy.java.uservices.entity.RestaurantEntity;
import com.h.udemy.java.uservices.exception.RestaurantDataAccessException;
import com.h.udemy.java.uservices.order.service.domain.entity.Product;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;
import com.h.udemy.java.uservices.order.service.domain.messages.I18n;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class RestaurantDataAccessMapper {


    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity =
                restaurantEntities
                        .stream()
                        .findFirst()
                        .orElseThrow(() ->
                        new RestaurantDataAccessException(I18n.ERR_RESTAURANT_NOT_FOUND.getMsg()));

        List<Product> restaurantProducts = restaurantEntities.stream().map(entity ->
                new Product(new ProductId(entity.getProductId()), entity.getProductName(),
                        new Money(entity.getProductPrice()))).toList();

        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}
