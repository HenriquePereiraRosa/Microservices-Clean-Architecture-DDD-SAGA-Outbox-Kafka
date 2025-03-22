package com.h.udemy.java.uservices.common.application.exception.handler.handler;

import com.h.udemy.java.uservices.common.application.exception.handler.handler.model.ErrorTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

import static com.h.udemy.java.uservices.domain.messages.log.LogExceptionMessages.UNEXPECTED_ERROR;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity handleException(Exception exception) {

        log.error(exception.getMessage(), exception);

        ErrorTo errorbody = ErrorTo.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(UNEXPECTED_ERROR.get())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorbody);
    }

    @ResponseBody
    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity handleValidationException(ValidationException exception) {

        if(exception instanceof ConstraintViolationException) {
            final String violations = this.extractViolations((ConstraintViolationException) exception);
            log.error(exception.getMessage(), exception);

            ErrorTo errorbody = ErrorTo.builder()
                    .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message(violations)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorbody);
        } else {
            log.error(exception.getMessage(), exception);

            ErrorTo errorBody = ErrorTo.builder()
                    .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message(exception.getMessage())
                    .build();

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorBody);

        }
    }

    private String extractViolations(ConstraintViolationException exception) {
        return exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(" -- "));
    }
}
