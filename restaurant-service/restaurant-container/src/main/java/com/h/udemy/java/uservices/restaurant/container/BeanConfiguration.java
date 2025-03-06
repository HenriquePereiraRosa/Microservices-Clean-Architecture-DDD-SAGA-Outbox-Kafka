package com.h.udemy.java.uservices.restaurant.container;


import com.h.udemy.java.uservices.restaurant.domain.core.RestaurantDomainServiceI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public RestaurantDomainServiceI restaurantDomainService() {
        return new RestaurantDomainServiceI();
    }
}
