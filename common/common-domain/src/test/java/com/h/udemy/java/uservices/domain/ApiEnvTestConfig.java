package com.h.udemy.java.uservices.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootTest(classes = BeanTestConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiEnvTestConfig {

    public ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    void setup() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void contextLoads() {
        log.info("common-domain ApiEnvTestConfig -- contextLoads --");
    }

}
