package com.example.gfood.consumerservice.domain;

import com.example.gfood.domain.ConsumerRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfig {
  @Bean
  public ConsumerService consumerService(ConsumerRepository consumerRepository) {
    return new ConsumerService(consumerRepository);
  }
}
