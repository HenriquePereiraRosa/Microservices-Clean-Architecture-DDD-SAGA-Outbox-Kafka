package com.h.udemy.java.uservices.data_access;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApiEnvTestConfig {
    public ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    void setup() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void contextLoads() {
       log.debug(I18n.APP_NAME_DESCRIPTION.getMsg());
       log.info(I18n.APP_NAME_DESCRIPTION.getMsg());
       log.warn(I18n.APP_NAME_DESCRIPTION.getMsg());
       log.error(I18n.APP_NAME_DESCRIPTION.getMsg());
    }

}
