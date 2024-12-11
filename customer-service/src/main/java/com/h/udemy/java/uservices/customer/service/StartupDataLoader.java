package com.h.udemy.java.uservices.customer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

//@Component
@Slf4j
@Profile("!prod")
public class StartupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final DataSource dataSource;

    public StartupDataLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            log.info("-- Inserting random sql db data --");
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("/db/startup/init-data.sql"));
            populator.execute(dataSource);
        } catch (Exception e) {
            log.error("-- Error inserting random sql db data: ", e);
        }
    }
}
