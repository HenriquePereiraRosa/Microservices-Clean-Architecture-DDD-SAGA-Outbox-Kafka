package com.h.udemy.java.uservices.order.service.domain.ports.output.repository;

import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;

import java.util.Optional;

public interface IRestaurantRepository {

    Optional<Restaurant> findInformation(Restaurant restaurant);
}
