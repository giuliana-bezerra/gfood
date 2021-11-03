package com.example.gfood.courierservice.domain;

import com.example.gfood.domain.CourierRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourierConfig {
  @Bean
  public CourierService courierService(CourierRepository courierRepository) {
    return new CourierService(courierRepository);
  }
}
