package com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.customer;


import com.h.udemy.java.uservices.order.service.domain.dto.message.CustomerModel;

public interface CustomerMessageListener {

    void customerCreated(CustomerModel customerModel);
}
