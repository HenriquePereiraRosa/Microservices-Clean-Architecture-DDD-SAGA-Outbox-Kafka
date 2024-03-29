package com.h.udemy.java.uservices.payment.service.messaging.test.util.factory;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;

import static com.h.udemy.java.uservices.domain.Constants.UTC_DATE_TIME_NOW;
import static com.h.udemy.java.uservices.payment.service.messaging.test.util.factory.PaymentFactory.createPayment;

public class PaymentEventFactory {
    private static final double PRICE_VALUE = 102.22;

    public static PaymentCompletedEvent createPaymentCompletedEvent(
            IDomainEventPublisher<PaymentCompletedEvent> eventPublisher) {

        return new PaymentCompletedEvent(createPayment(PRICE_VALUE),
                UTC_DATE_TIME_NOW,
                eventPublisher);
    }
}
