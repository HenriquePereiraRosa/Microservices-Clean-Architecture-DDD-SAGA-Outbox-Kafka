package com.h.udemy.java.uservices.customer.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Locale;

import static com.h.udemy.java.uservices.customer.service.CustomerServiceApplication.CUSTOMER_MODULE_PACKAGE_DATAACCESS;
import static com.h.udemy.java.uservices.customer.service.CustomerServiceApplication.PARENT_MODULE_PACKAGE_DATAACCESS;
import static com.h.udemy.java.uservices.domain.Constants.BASE_PACKAGE;

@EnableJpaRepositories(basePackages = {PARENT_MODULE_PACKAGE_DATAACCESS, CUSTOMER_MODULE_PACKAGE_DATAACCESS})
@EntityScan(basePackages = {PARENT_MODULE_PACKAGE_DATAACCESS, CUSTOMER_MODULE_PACKAGE_DATAACCESS})
@SpringBootApplication(scanBasePackages = BASE_PACKAGE)
public class CustomerServiceApplication {

    public static final String MODULE_PACKAGE = BASE_PACKAGE + ".customer.service";
    public static final String CUSTOMER_MODULE_PACKAGE_DATAACCESS = MODULE_PACKAGE + ".dataaccess";
    public static final String PARENT_MODULE_PACKAGE_DATAACCESS = BASE_PACKAGE + ".dataaccess";

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);

        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }
}