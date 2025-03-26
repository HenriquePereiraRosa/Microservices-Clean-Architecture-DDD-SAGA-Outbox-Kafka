package com.h.udemy.java.uservices.customer.service;


import com.h.udemy.java.uservices.customer.service.domain.CustomerDomainService;
import com.h.udemy.java.uservices.customer.service.domain.CustomerDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CustomerDomainService customerDomainService() {
        return new CustomerDomainServiceImpl();
    }
}
