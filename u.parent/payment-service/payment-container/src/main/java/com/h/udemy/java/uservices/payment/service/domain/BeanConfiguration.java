package com.h.udemy.java.uservices.payment.service.domain;


import com.h.udemy.java.uservices.payment.domain.core.PaymentDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public PaymentDomainService paymentDomainService() {
        return new PaymentDomainService();
    }
}
