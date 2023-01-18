package com.h.udemy.java.uservices.test.util.factory;

import com.h.udemy.java.uservices.order.service.domain.entity.Customer;

import static com.h.udemy.java.uservices.test.util.ConstTestUtils.CUSTOMER_ID;

public class CustomerFactory {
    static public Customer createCustomer() {

        return new Customer(CUSTOMER_ID);
    }
}
