package com.h.udemy.java.uservices.application.exception.handler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorDTO {

    private final String code;
    private final String message;
}
