package com.h.udemy.java.uservices.messaging.mapper;

import com.h.udemy.java.uservices.core.event.CustomerCreatedEvent;
import com.h.udemy.java.uservices.kafka.order.avro.model.CustomerAvroModel;
import org.springframework.stereotype.Component;

@Component
public class CustomerMessagingDataMapper {

    public CustomerAvroModel paymentResponseAvroModelToPaymentResponse(CustomerCreatedEvent
                                                                               customerCreatedEvent) {
        return CustomerAvroModel.newBuilder()
                .setId(customerCreatedEvent.getCustomer().getId().getValue().toString())
                .setUsername(customerCreatedEvent.getCustomer().getUsername())
                .setFirstName(customerCreatedEvent.getCustomer().getFirstName())
                .setLastName(customerCreatedEvent.getCustomer().getLastName())
                .build();
    }
}
