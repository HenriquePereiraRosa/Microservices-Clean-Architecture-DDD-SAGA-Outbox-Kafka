package com.h.udemy.java.uservices.order.service.application.exception.handler;

import com.h.udemy.java.uservices.common.application.exception.handler.handler.GlobalExceptionHandler;
import com.h.udemy.java.uservices.common.application.exception.handler.handler.model.ErrorTo;
import com.h.udemy.java.uservices.order.service.domain.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class OrderApplicationExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(value = {OrderDomainException.class})
    public ResponseEntity<ErrorTo> handleOrderDomainException(OrderDomainException orderDomainException) {

        log.error(orderDomainException.getMessage(), orderDomainException);

        ErrorTo errorTo = ErrorTo.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(orderDomainException.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorTo);
    }

    @ExceptionHandler(value = {OrderNotFoundException.class})
    public ResponseEntity<ErrorTo> handleOrderNotFoundException(OrderNotFoundException orderNotFoundException) {

        log.error(orderNotFoundException.getMessage(), orderNotFoundException);

        ErrorTo errorTo = ErrorTo.builder()
                .code(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(orderNotFoundException.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorTo);
    }

    @ExceptionHandler(value = {OrderCouldNotBeSavedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorTo> handleOrderCouldNotBeSavedException(OrderCouldNotBeSavedException exception) {

        log.error(exception.getMessage(), exception);

        ErrorTo errorTo = ErrorTo.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorTo);
    }

    @ExceptionHandler(value = {CustomerNotFoundException.class})
    public ResponseEntity<ErrorTo> handleCustomerNotFoundException(CustomerNotFoundException exception) {

        log.error(exception.getMessage(), exception);

        ErrorTo errorTo = ErrorTo.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorTo);
    }

    @ExceptionHandler(value = {UnableToPublishOrderCreationMessageException.class})
    public ResponseEntity<ErrorTo> handleCustomerNotFoundException(UnableToPublishOrderCreationMessageException exception) {

        log.error(exception.getMessage(), exception);

        ErrorTo errorTo = ErrorTo.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorTo);
    }
}
