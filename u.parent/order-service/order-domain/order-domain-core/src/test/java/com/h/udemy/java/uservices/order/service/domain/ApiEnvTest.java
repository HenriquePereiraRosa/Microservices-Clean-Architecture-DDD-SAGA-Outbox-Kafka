package com.h.udemy.java.uservices.order.service.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.h.udemy.java.uservices.order.service.domain.exception.msg.I18n;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class ApiEnvTest {

    private static final Logger log = Logger.getLogger("Main.class");

    //    @Autowired
//    public
//    Environment env;
//    @Autowired
//    DataSource dataSource;
//    @Autowired
//    public MockMvc mockMvc;
    @Autowired
    private ApplicationContext appContext;

    public ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    void setup() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void contextLoads() {
        try {
            log.info("-- DB Setup --");
            // todo: fix @Autowireds resolution
            I18n.setMessageSource(appContext.getBean("messageSource"));
            I18n.ERR_ORDER_TOTAL_AND_ORDER_PRICES_DIFF.getMsg(); // todo: remove this
//            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//            populator.addScript(new ClassPathResource("/db/startup/startup_data.sql"));
//            populator.execute(dataSource);
//        } catch (ScriptStatementFailedException e) {
//            log.error("-- Error inserting random sql db data: ", e);
        } catch (Exception e) {
            log.log(Level.SEVERE, "-- Error inserting random sql db data: ", e);
        }

    }


}
