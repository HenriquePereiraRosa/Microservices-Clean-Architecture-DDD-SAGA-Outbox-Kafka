package com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentFailedEvent;

public interface IPaymentFailedMessagePublisher extends IDomainEventPublisher<PaymentFailedEvent> {
}
