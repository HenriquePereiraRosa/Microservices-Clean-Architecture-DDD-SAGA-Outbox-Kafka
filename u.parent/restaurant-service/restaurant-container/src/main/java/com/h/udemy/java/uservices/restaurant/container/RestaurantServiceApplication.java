package com.h.udemy.java.uservices.restaurant.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.h.udemy.java.uservices.domain.Constants.BASE_PACKAGE;
import static com.h.udemy.java.uservices.restaurant.container.RestaurantServiceApplication.MODULE_PACKAGE;

@EnableJpaRepositories(basePackages = BASE_PACKAGE + "." + MODULE_PACKAGE)
@EntityScan(basePackages = BASE_PACKAGE + "." + MODULE_PACKAGE)
@SpringBootApplication(scanBasePackages = BASE_PACKAGE)
public class RestaurantServiceApplication {
    public static final String MODULE_PACKAGE = "restaurant.dataaccess";
    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class, args);
    }
}