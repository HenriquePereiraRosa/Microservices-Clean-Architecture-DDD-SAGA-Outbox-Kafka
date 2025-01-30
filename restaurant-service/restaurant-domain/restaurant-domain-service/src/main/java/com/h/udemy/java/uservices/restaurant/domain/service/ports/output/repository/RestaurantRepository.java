package com.h.udemy.java.uservices.restaurant.domain.service.ports.output.repository;

import com.h.udemy.java.uservices.restaurant.domain.core.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);

}
