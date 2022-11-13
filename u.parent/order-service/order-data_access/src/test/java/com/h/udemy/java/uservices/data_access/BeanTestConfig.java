package com.h.udemy.java.uservices.data_access;

import com.h.udemy.java.uservices.data_access.order.repository.IOrderJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class BeanTestConfig {

    @Bean
    public IOrderJpaRepository orderJpaRepository() {
        return Mockito.mock(IOrderJpaRepository.class);
    }

}
