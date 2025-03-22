package com.h.udemy.java.uservices.application.handler;

import com.h.udemy.java.uservices.common.application.exception.handler.handler.GlobalExceptionHandler;
import com.h.udemy.java.uservices.common.application.exception.handler.handler.model.ErrorTo;
import com.h.udemy.java.uservices.core.exception.CustomerDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class CustomerGlobalExceptionHandler extends GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {CustomerDomainException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorTo handleException(CustomerDomainException exception) {
        log.error(exception.getMessage(), exception);
        return ErrorTo.builder().code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage()).build();
    }

}
