package com.h.udemy.java.uservices.payment.domain.core.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootTest(classes = BeanTestConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class ApiEnvTest {
    @Value( "${app.name.description}" )
    private String APP_NAME_DESCRIPTION;
    public ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public MockMvc mockMvc;


    @PostConstruct
    void setup() {
        MockitoAnnotations.initMocks(this);

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void contextLoads() {
       log.debug(APP_NAME_DESCRIPTION);
       log.info(APP_NAME_DESCRIPTION);
       log.warn(APP_NAME_DESCRIPTION);
       log.error(APP_NAME_DESCRIPTION);
    }

}
