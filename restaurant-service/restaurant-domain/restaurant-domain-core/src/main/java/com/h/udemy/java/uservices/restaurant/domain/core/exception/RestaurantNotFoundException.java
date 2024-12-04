package com.h.udemy.java.uservices.restaurant.domain.core.exception;

import com.h.udemy.java.uservices.domain.DomainException;

public class RestaurantNotFoundException extends DomainException {
    public RestaurantNotFoundException(String message) {
        super(message);
    }
}