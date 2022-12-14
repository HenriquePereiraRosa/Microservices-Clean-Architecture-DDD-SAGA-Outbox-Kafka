package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.domain.messages.Msgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

@SpringBootApplication(scanBasePackages = "com.h.udemy.java.uservices")
public class Main {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        ApplicationContext context =  SpringApplication.run(Main.class, args);

        System.out.println(Msgs.ERR_ORDER_ITEM_PRICE_INVALID.get());
    }
}