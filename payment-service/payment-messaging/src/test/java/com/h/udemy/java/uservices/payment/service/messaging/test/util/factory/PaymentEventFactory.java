package com.h.udemy.java.uservices.payment.service.messaging.test.util.factory;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;

import static com.h.udemy.java.uservices.domain.Constants.ZONED_DATE_TIME;
import static com.h.udemy.java.uservices.payment.service.messaging.test.util.factory.PaymentFactory.createPayment;

public class PaymentEventFactory {
    private static final double PRICE_VALUE = 102.22;

    public static PaymentCompletedEvent createPaymentCompletedEvent(
            DomainEventPublisher<PaymentCompletedEvent> eventPublisher) {

        return new PaymentCompletedEvent(createPayment(PRICE_VALUE),
                ZONED_DATE_TIME,
                eventPublisher);
    }
}
