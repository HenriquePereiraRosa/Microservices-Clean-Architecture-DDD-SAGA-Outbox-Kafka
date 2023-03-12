package com.h.udemy.java.uservices.payment.service.messaging.test.config;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCancelledEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentFailedEvent;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.IPaymentCancelledMessagePublisher;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.IPaymentCompletedMessagePublisher;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.IPaymentFailedMessagePublisher;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.ICreditEntryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.ICreditHistoryRepository;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.IPaymentRepository;
import org.mockito.Mockito;
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

    @Bean
    public IPaymentCompletedMessagePublisher iPaymentCompletedMessagePublisher() {
        return Mockito.mock(IPaymentCompletedMessagePublisher.class);
    }
    @Bean
    public IPaymentCancelledMessagePublisher iPaymentCancelledMessagePublisher() {
        return Mockito.mock(IPaymentCancelledMessagePublisher.class);
    }
    @Bean
    public IPaymentFailedMessagePublisher iPaymentFailedMessagePublisher() {
        return Mockito.mock(IPaymentFailedMessagePublisher.class);
    }

    @Bean
    public IPaymentRepository iPaymentRepository() {
        return Mockito.mock(IPaymentRepository.class);
    }

    @Bean
    public ICreditEntryRepository iCreditEntryRepository() {
        return Mockito.mock(ICreditEntryRepository.class);
    }

    @Bean
    public ICreditHistoryRepository iCreditHistoryRepository() {
        return Mockito.mock(ICreditHistoryRepository.class);
    }
}
