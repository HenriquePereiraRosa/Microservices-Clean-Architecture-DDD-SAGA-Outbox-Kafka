package com.h.udemy.java.uservices.exception.handler;

import com.h.udemy.java.uservices.application.exception.handler.GlobalExceptionHandler;
import com.h.udemy.java.uservices.application.exception.handler.model.ErrorDTO;
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
    public ResponseEntity handleOrderDomainException(OrderDomainException orderDomainException) {

        log.error(orderDomainException.getMessage(), orderDomainException);

        ErrorDTO errorbody = ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(orderDomainException.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorbody);
    }

    @ExceptionHandler(value = {OrderNotFoundException.class})
    public ResponseEntity handleOrderNotFoundException(OrderNotFoundException orderNotFoundException) {

        log.error(orderNotFoundException.getMessage(), orderNotFoundException);

        ErrorDTO errorbody = ErrorDTO.builder()
                .code(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(orderNotFoundException.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorbody);
    }

    @ExceptionHandler(value = {OrderCouldNotBeSavedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleOrderCouldNotBeSavedException(OrderCouldNotBeSavedException exception) {

        log.error(exception.getMessage(), exception);

        ErrorDTO errorbody = ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorbody);
    }

    @ExceptionHandler(value = {CustomerNotFoundException.class})
    public ResponseEntity handleCustomerNotFoundException(CustomerNotFoundException exception) {

        log.error(exception.getMessage(), exception);

        ErrorDTO errorbody = ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorbody);
    }

    @ExceptionHandler(value = {UnableToPublishOrderCreationMessageException.class})
    public ResponseEntity handleCustomerNotFoundException(UnableToPublishOrderCreationMessageException exception) {

        log.error(exception.getMessage(), exception);

        ErrorDTO errorBody = ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }
}
