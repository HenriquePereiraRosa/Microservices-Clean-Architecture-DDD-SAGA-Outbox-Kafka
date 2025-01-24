package com.h.udemy.java.uservices.order.service.dataaccess;

import com.h.udemy.java.uservices.restaurant.dataaccess.repository.IRestaurantJpaRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IOrderRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootApplication(scanBasePackages = "com.h.udemy.java.uservices")
public class BeanTestConfig {

    @Bean
    public OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher() {
        return Mockito.mock(OrderCreatedPaymentRequestMessagePublisher.class);
    }

    @Bean
    public OrderCancelledPaymentRequestRequestMessagePublisher orderCancelledPaymentRequestRequestMessagePublisher() {
        return Mockito.mock(OrderCancelledPaymentRequestRequestMessagePublisher.class);
    }

    @Bean
    public OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestRequestMessagePublisher() {
        return Mockito.mock(OrderPaidRestaurantRequestMessagePublisher.class);
    }

    @Bean
    public IRestaurantJpaRepository restaurantJpaRepository() {
        return Mockito.mock(IRestaurantJpaRepository.class);
    }

    @Bean
    @Primary
    public IOrderRepository orderJpaRepository() {
        return Mockito.mock(IOrderRepository.class);
    }

}
