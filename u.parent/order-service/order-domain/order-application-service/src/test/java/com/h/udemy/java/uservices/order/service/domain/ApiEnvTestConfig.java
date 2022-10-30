package com.h.udemy.java.uservices.order.service.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.h.udemy.java.uservices.order.service.domain.messages.I18n;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootTest(classes = BeanTestConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApiEnvTestConfig {

    @Autowired
    private ApplicationContext appContext;
    @Autowired
    private MessageSource messageSource;

    public ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    void setup() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @Order(0)
    void contextLoads() {
        // todo: fix @Autowireds resolution
        I18n.setMessageSource(appContext.getBean("messageSource"));
        log.info(I18n.APP_NAME_DESCRIPTION.getMsg()); // todo: remove this
    }
}
