package com.h.udemy.java.uservices.customer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@EnableJpaRepositories(basePackages = "com.h.udemy.java.uservices.order.service.dataaccess")
@EntityScan(basePackages = "com.h.udemy.java.uservices.customer.service.dataaccess")
@SpringBootApplication(scanBasePackages = "com.h.udemy.java.uservices")
public class CustomerServiceApi {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApi.class, args);
        log.info("CustomerServiceApi Running...");
    }
}