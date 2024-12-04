package com.h.udemy.java.uservices.payment.domain.core.test.config;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCancelledEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentFailedEvent;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.h.udemy.java.uservices")
public class BeanTestConfig {

    @Bean
    public IDomainEventPublisher<PaymentCompletedEvent> completedEventPublisher() {
        return Mockito.mock(IDomainEventPublisher.class);
    }

    @Bean
    public IDomainEventPublisher<PaymentCancelledEvent> cancelledEventPublisher() {
        return Mockito.mock(IDomainEventPublisher.class);
    }

    @Bean
    public IDomainEventPublisher<PaymentFailedEvent> failedEventPublisher() {
        return Mockito.mock(IDomainEventPublisher.class);
    }
}
