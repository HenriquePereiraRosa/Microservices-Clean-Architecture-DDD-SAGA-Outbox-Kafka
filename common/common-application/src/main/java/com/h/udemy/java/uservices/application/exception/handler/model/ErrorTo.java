package com.h.udemy.java.uservices.application.exception.handler.model;

import lombok.Builder;


@Builder
public record ErrorTo(String code, String message) {

}
