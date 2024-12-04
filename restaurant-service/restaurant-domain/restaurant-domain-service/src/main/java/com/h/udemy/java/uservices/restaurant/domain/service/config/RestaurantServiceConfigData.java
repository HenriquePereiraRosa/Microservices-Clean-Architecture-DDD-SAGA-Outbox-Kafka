package com.h.udemy.java.uservices.restaurant.domain.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("restaurant-service")
public class RestaurantServiceConfigData {

    private String restaurantApprovalRequestTopicName;
    private String restaurantApprovalResponseTopicName;

}
