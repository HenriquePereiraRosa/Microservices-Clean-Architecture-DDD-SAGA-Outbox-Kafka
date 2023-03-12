package com.h.udemy.java.uservices.order.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.h.udemy.java.uservices.domain.Constants.BASE_PACKAGE;

@Slf4j
@EnableJpaRepositories(basePackages = {BASE_PACKAGE})
@EntityScan(basePackages = BASE_PACKAGE)
@SpringBootApplication(scanBasePackages = BASE_PACKAGE)
public class OrderServiceApi {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApi.class, args);
        log.info("OrderServiceApi Running...");
    }
}