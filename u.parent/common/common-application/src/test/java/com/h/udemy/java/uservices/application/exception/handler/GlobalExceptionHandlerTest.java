package com.h.udemy.java.uservices.application.exception.handler;

import com.h.udemy.java.uservices.application.ApiEnvTest;
import com.h.udemy.java.uservices.application.exception.handler.model.ErrorDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest extends ApiEnvTest {

    private static String ERROR_MSG = "Exception Message";

    GlobalExceptionHandler handler;

    @PostConstruct
    void setup() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void should_return_500() {
        ResponseEntity response = handler.handleException(new Exception(ERROR_MSG));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void should_return_400() {
        ResponseEntity<ErrorDTO> response = handler
                .handleValidationException(new ValidationException(ERROR_MSG));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ERROR_MSG, response.getBody().getMessage());
    }

    @Test
    void when_with_ViolationMessages_should_return_400_with_msgs_concat() {

        Set<ConstraintViolation> violations = new HashSet<>();

        ResponseEntity<ErrorDTO> response = handler
                .handleValidationException(new ValidationException(ERROR_MSG));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ERROR_MSG, response.getBody().getMessage());
    }
}