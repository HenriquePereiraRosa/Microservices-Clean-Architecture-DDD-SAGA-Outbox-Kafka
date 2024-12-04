package com.h.udemy.java.uservices.payment.service.domain;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.h.udemy.java.uservices.domain.Constants.BASE_PACKAGE;
import static com.h.udemy.java.uservices.payment.service.domain.PaymentServiceApplication.MODULE_PACKAGE;

@EnableJpaRepositories(basePackages = BASE_PACKAGE + "." + MODULE_PACKAGE)
@EntityScan(basePackages = BASE_PACKAGE + "." + MODULE_PACKAGE)
@SpringBootApplication(scanBasePackages = BASE_PACKAGE)
public class PaymentServiceApplication {


    public static final String MODULE_PACKAGE = "payment.service.dataaccess";

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}
