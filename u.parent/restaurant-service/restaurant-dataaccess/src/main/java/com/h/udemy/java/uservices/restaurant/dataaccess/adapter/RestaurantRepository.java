package com.h.udemy.java.uservices.restaurant.dataaccess.adapter;

import com.h.udemy.java.uservices.dataaccess.restaurant.entity.RestaurantEntity;
import com.h.udemy.java.uservices.dataaccess.restaurant.repository.IRestaurantJpaRepository;
import com.h.udemy.java.uservices.restaurant.dataaccess.mapper.RestaurantDataAccessMapper;
import com.h.udemy.java.uservices.restaurant.domain.core.entity.Restaurant;
import com.h.udemy.java.uservices.restaurant.domain.service.ports.output.repository.IRestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepository implements IRestaurantRepository {

    private final IRestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public RestaurantRepository(IRestaurantJpaRepository restaurantJpaRepository,
                                RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts =
                restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository
                .findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(),
                        restaurantProducts);
        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
