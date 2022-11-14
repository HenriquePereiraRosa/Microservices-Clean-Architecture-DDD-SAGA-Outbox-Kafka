package com.h.udemy.java.uservices.dataaccess;

import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment.IOrderCancelledPaymentRequestRequestMessagePublisher;
import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment.IOrderCreatedPaymentRequestMessagePublisher;
import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment.IOrderPaidRestaurantRequestRequestMessagePublisher;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.ICustomerRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IOrderRepository;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IRestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.h.udemy.java.uservices")
public class BeanTestConfig {

    @Bean
    public IOrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher() {
        return Mockito.mock(IOrderCreatedPaymentRequestMessagePublisher.class);
    }

    @Bean
    public IOrderCancelledPaymentRequestRequestMessagePublisher orderCancelledPaymentRequestRequestMessagePublisher() {
        return Mockito.mock(IOrderCancelledPaymentRequestRequestMessagePublisher.class);
    }

    @Bean
    public IOrderPaidRestaurantRequestRequestMessagePublisher orderPaidRestaurantRequestRequestMessagePublisher() {
        return Mockito.mock(IOrderPaidRestaurantRequestRequestMessagePublisher.class);
    }

    @Bean
    public ICustomerRepository customerRepository() {
        return Mockito.mock(ICustomerRepository.class);
    }

    @Bean
    public IRestaurantRepository restaurantRepository() {
        return Mockito.mock(IRestaurantRepository.class);
    }

    @Bean
    @Primary
    public IOrderRepository orderJpaRepository() {
        return Mockito.mock(IOrderRepository.class);
    }

}
