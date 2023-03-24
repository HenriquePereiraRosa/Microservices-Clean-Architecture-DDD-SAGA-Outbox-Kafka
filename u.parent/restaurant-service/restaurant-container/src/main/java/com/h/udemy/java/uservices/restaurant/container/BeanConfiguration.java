package com.h.udemy.java.uservices.restaurant.container;


import com.h.udemy.java.uservices.restaurant.domain.core.RestaurantDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public RestaurantDomainService restaurantDomainService() {
        return new RestaurantDomainService();
    }
}
