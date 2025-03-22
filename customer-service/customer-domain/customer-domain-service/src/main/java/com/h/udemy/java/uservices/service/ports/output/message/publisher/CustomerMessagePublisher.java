package com.h.udemy.java.uservices.service.ports.output.message.publisher;

import com.h.udemy.java.uservices.core.event.CustomerCreatedEvent;

public interface CustomerMessagePublisher {

    void publish(CustomerCreatedEvent customerCreatedEvent);

}