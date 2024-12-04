package com.h.udemy.java.uservices.restaurant.domain.core.exception;

import com.h.udemy.java.uservices.domain.DomainException;

public class RestaurantDomainException extends DomainException {
    public RestaurantDomainException(String message) {
        super(message);
    }
}