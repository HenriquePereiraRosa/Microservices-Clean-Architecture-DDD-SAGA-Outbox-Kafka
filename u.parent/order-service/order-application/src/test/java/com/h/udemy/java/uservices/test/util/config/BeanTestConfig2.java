package com.h.udemy.java.uservices.test.util.config;

import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.ICustomerRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IOrderRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IRestaurantRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.h.udemy.java.uservices")
public class BeanTestConfig2 extends BeanTestConfig {

    @Bean
    public IOrderRepository orderRepository() {
        return Mockito.mock(IOrderRepository.class);
    }

    @Bean
    public ICustomerRepository customerRepository() {
        return Mockito.mock(ICustomerRepository.class);
    }

    @Bean
    public IRestaurantRepository restaurantRepository() {
        return Mockito.mock(IRestaurantRepository.class);
    }

}
