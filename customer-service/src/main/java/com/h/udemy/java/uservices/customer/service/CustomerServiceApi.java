package com.h.udemy.java.uservices.customer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Locale;

@Slf4j
@EnableJpaRepositories(basePackages = "com.h.udemy.java.uservices")
@EntityScan(basePackages = "com.h.udemy.java.uservices")
@SpringBootApplication(scanBasePackages = "com.h.udemy.java.uservices")
public class CustomerServiceApi {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApi.class, args);

        LocaleContextHolder.setLocale(Locale.ENGLISH);

        log.info("CustomerServiceApi Running...");
    }
}