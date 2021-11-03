package com.example.gfood.restaurantservice.domain;

import com.example.gfood.domain.RestaurantRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantConfig {
  @Bean
  public RestaurantService restaurantService(RestaurantRepository restaurantRepository) {
    return new RestaurantService(restaurantRepository);
  }
}
