package com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment;

import com.h.udemy.java.uservices.domain.event.DomainPublisherEvent;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;

public interface IOrderCancelledPaymentRequestRequestMessagePublisher
        extends DomainPublisherEvent<OrderPaidEvent> {

}
