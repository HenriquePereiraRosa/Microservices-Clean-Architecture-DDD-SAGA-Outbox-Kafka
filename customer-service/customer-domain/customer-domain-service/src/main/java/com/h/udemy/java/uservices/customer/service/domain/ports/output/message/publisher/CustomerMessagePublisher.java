package com.h.udemy.java.uservices.customer.service.domain.ports.output.message.publisher;

import com.h.udemy.java.uservices.customer.service.domain.event.CustomerCreatedEvent;

public interface CustomerMessagePublisher {

    void publish(CustomerCreatedEvent customerCreatedEvent);

}