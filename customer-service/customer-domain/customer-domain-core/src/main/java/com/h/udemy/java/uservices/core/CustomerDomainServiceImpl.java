package com.h.udemy.java.uservices.core;

import com.h.udemy.java.uservices.core.event.CustomerCreatedEvent;
import com.h.udemy.java.uservices.core.entity.Customer;
import lombok.extern.slf4j.Slf4j;

import static com.h.udemy.java.uservices.domain.Constants.getZonedDateTimeNow;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.CUSTOMER_ID_INITIATED;

@Slf4j
public class CustomerDomainServiceImpl implements CustomerDomainService {

    public CustomerCreatedEvent validateAndInitiateCustomer(Customer customer) {
        //Any Business logic required to run for a customer creation
        log.info(CUSTOMER_ID_INITIATED.build(customer.getId().getValue()));

        return new CustomerCreatedEvent(customer, getZonedDateTimeNow());
    }
}

