package com.h.udemy.java.uservices.application.exception.handler;

import com.h.udemy.java.uservices.application.exception.handler.model.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import java.util.stream.Collectors;

import static com.h.udemy.java.uservices.domain.log.LogExceptionMessages.UNEXPECTED_ERROR;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(Exception exception) {

        log.error(exception.getMessage(), exception);

        return ErrorDTO.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(UNEXPECTED_ERROR.get())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(ValidationException exception) {

        if(exception instanceof ConstraintViolationException) {
            final String violations = extractViolations((ConstraintViolationException) exception);
            log.error(exception.getMessage(), exception);

            return ErrorDTO.builder()
                    .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message(violations)
                    .build();
        } else {
            log.error(exception.getMessage(), exception);

            return ErrorDTO.builder()
                    .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message(exception.getMessage())
                    .build();

        }
    }

    private String extractViolations(ConstraintViolationException exception) {
        return exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(" -- "));
    }
}
