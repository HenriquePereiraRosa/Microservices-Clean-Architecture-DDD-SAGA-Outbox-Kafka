package com.h.udemy.java.uservices.payment.domain.core.test.config;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCancelledEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentFailedEvent;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.h.udemy.java.uservices")
public class BeanTestConfig {

    @Bean
    public DomainEventPublisher<PaymentCompletedEvent> completedEventPublisher() {
        return Mockito.mock(DomainEventPublisher.class);
    }

    @Bean
    public DomainEventPublisher<PaymentCancelledEvent> cancelledEventPublisher() {
        return Mockito.mock(DomainEventPublisher.class);
    }

    @Bean
    public DomainEventPublisher<PaymentFailedEvent> failedEventPublisher() {
        return Mockito.mock(DomainEventPublisher.class);
    }
}
