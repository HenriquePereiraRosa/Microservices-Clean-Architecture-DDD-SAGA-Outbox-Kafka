package com.h.udemy.java.uservices.order.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@EnableJpaRepositories(basePackages = "com.h.udemy.java.uservices.order.service.dataaccess")
@EntityScan(basePackages = "com.h.udemy.java.uservices.order.service.dataaccess")
@SpringBootApplication(scanBasePackages = "com.h.udemy.java.uservices")
public class OrderServiceApi {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApi.class, args);
        log.info("OrderServiceApi Running...");
    }
}