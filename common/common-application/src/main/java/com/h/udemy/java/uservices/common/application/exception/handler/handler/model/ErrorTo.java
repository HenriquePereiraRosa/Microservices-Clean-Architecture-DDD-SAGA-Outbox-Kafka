package com.h.udemy.java.uservices.common.application.exception.handler.handler.model;

import lombok.Builder;


@Builder
public record ErrorTo(String code, String message) {

}
