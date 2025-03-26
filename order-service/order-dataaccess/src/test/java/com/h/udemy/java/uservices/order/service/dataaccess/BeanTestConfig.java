package com.h.udemy.java.uservices.order.service.dataaccess;

import com.h.udemy.java.uservices.common.dataaccess.repository.RestaurantJpaRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.OrderRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootApplication(scanBasePackages = "com.h.udemy.java.uservices.order.service.dataaccess")
public class BeanTestConfig {

    @Bean
    public RestaurantJpaRepository restaurantJpaRepository() {
        return Mockito.mock(RestaurantJpaRepository.class);
    }

    @Bean
    @Primary
    public OrderRepository orderJpaRepository() {
        return Mockito.mock(OrderRepository.class);
    }

}
