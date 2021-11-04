package com.example.gfood.orderservice.domain;

import com.example.gfood.consumerservice.domain.ConsumerService;
import com.example.gfood.domain.OrderRepository;
import com.example.gfood.domain.RestaurantRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {
  @Bean
  public OrderService orderService(OrderRepository orderRepository, RestaurantRepository restaurantRepository,
      ConsumerService consumerService) {
    return new OrderService(orderRepository, restaurantRepository, consumerService);
  }
}
