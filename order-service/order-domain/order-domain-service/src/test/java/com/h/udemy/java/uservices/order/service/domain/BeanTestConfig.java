package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.ICustomerRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IOrderRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IRestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
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
