package com.h.udemy.java.uservices.order.service.application.exception.handler;

import com.h.udemy.java.uservices.order.service.application.test.util.config.BeanTestConfig;
import com.h.udemy.java.uservices.order.service.domain.exception.CustomerNotFoundException;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderCouldNotBeSavedException;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainException;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import javax.validation.ValidationException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = BeanTestConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderApplicationExceptionHandlerTest {

    private static final String ERROR_MSG = "Exception Test Error Message";

    OrderApplicationExceptionHandler handler;

    @PostConstruct
    void setup() {
        handler = new OrderApplicationExceptionHandler();
    }

    @Test
    void when_OrderDomainException_should_return_400 () {
        ResponseEntity response = handler
                .handleOrderDomainException(new OrderDomainException(ERROR_MSG));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void when_ValidationException_should_return_400() {
        ResponseEntity response = handler
                .handleValidationException(new ValidationException(ERROR_MSG));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void when_OrderNotFoundException_should_return_404() {
        ResponseEntity response = handler
                .handleOrderNotFoundException(new OrderNotFoundException(ERROR_MSG));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void when_OrderCouldNotBeSavedException_should_return_400() {
        ResponseEntity response = handler
                .handleOrderCouldNotBeSavedException(new OrderCouldNotBeSavedException(UUID.randomUUID()));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void when_CustomerNotFoundException_should_return_404() {
        ResponseEntity response = handler
                .handleCustomerNotFoundException(new CustomerNotFoundException(UUID.randomUUID()));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}