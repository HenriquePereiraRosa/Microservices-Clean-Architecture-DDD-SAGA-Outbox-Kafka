package com.h.udemy.java.uservices.restaurant.domain.service.exception;

import com.h.udemy.java.uservices.domain.DomainException;


public class RestaurantApplicationServiceException extends DomainException {

    public RestaurantApplicationServiceException(String message) {
        super(message);
    }
}
