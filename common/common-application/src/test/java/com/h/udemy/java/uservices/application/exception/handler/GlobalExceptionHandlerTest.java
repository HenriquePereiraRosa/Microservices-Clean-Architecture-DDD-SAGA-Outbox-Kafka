package com.h.udemy.java.uservices.application.exception.handler;

import com.h.udemy.java.uservices.application.ApiEnvTest;
import com.h.udemy.java.uservices.application.exception.handler.model.ErrorDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest extends ApiEnvTest {

    private static final Object[] EMPTY_ARGS = { null, null };
    private static final String ERROR_MSG = "Exception Message";
    private static final String ERROR_MSG_EMAIL = "Exception Message - EMAIL INVALID";

    GlobalExceptionHandler handler;

    @Autowired
    private Validator validator;
    @Autowired
    private ApplicationContext appContext;

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
        assertEquals(ERROR_MSG, response.getBody().message());
    }

    @Test
    void when_with_ViolationMessages_should_return_400_with_msgs_concat() {
        Email email = new Email("invalid.email.server.com");
        Set<ConstraintViolation<Email>> violations = validator.validate(email);

        ResponseEntity<ErrorDTO> response = handler
                .handleValidationException(new ConstraintViolationException(
                        new HashSet<ConstraintViolation<?>>(violations)));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ERROR_MSG_EMAIL, response.getBody().message());
    }

    private class Email{
        @Pattern(regexp = "^(.+)@(.+)$", message = ERROR_MSG_EMAIL)
        private String email;

        public Email(String email) {
            this.email = email;
        }
    }
}