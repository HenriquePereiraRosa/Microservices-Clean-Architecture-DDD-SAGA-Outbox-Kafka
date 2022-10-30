package com.h.udemy.java.uservices.order.service.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.h.udemy.java.uservices.order.service.domain.messages.I18n;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApiEnvTestConfig {

    //    @Autowired
//    public
//    Environment env;
//    @Autowired
//    DataSource dataSource;
//    @Autowired
//    public MockMvc mockMvc;
    @Autowired
    private ApplicationContext appContext;

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.ENGLISH);

        return messageSource;
    }

    public ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    void setup() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // todo: fix @Autowireds resolution
        I18n.setMessageSource(appContext.getBean("messageSource"));
        log.info(I18n.ERR_ORDER_TOTAL_AND_ORDER_PRICES_DIFF.getMsg()); // todo: remove this
    }

    @Test
    void contextLoads() {
        try {
            log.info("-- DB Setup --");
//            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//            populator.addScript(new ClassPathResource("/db/startup/startup_data.sql"));
//            populator.execute(dataSource);
        } catch (Exception e) {
            log.error("-- Error inserting random sql db data ", e);
        }

    }

}
