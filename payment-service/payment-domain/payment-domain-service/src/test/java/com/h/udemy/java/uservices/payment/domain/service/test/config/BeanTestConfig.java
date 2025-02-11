package com.h.udemy.java.uservices.payment.domain.service.test.config;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCancelledEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentFailedEvent;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.PaymentResponseMessagePublisher;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.CreditEntryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.CreditHistoryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.OrderOutboxRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.PaymentRepository;
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

    @Bean
    public OrderOutboxRepository orderOutboxRepository() {
        return Mockito.mock(OrderOutboxRepository.class);
    }

    @Bean
    public PaymentResponseMessagePublisher PaymentResponseMessagePublisher() {
        return Mockito.mock(PaymentResponseMessagePublisher.class);
    }

    @Bean
    public PaymentRepository paymentRepository() {
        return Mockito.mock(PaymentRepository.class);
    }

    @Bean
    public CreditEntryRepository creditEntryRepository() {
        return Mockito.mock(CreditEntryRepository.class);
    }

    @Bean
    public CreditHistoryRepository creditHistoryRepository() {
        return Mockito.mock(CreditHistoryRepository.class);
    }
}
