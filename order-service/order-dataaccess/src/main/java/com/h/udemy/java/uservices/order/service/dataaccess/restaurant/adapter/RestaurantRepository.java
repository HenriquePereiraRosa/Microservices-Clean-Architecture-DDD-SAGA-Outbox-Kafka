package com.h.udemy.java.uservices.order.service.dataaccess.restaurant.adapter;

import com.h.udemy.java.uservices.restaurant.dataaccess.entity.RestaurantEntity;
import com.h.udemy.java.uservices.restaurant.dataaccess.repository.IRestaurantJpaRepository;
import com.h.udemy.java.uservices.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepository implements IRestaurantRepository {

    @Autowired
    private IRestaurantJpaRepository restaurantJpaRepository;

    @Autowired
    private RestaurantDataAccessMapper restaurantDataAccessMapper;


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
