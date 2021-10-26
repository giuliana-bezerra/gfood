package com.example.gfood.consumerservice.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfig {
  @Bean
  public ConsumerService consumerService() {
    return new ConsumerService();
  }
}
