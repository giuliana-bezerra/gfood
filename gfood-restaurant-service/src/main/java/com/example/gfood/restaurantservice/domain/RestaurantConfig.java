package com.example.gfood.restaurantservice.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantConfig {
  @Bean
  public RestaurantService restaurantService() {
    return new RestaurantService();
  }
}
