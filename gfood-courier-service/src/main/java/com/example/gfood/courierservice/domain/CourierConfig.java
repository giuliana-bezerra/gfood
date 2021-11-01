package com.example.gfood.courierservice.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourierConfig {
  @Bean
  public CourierService courierService() {
    return new CourierService();
  }
}
