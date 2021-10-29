package com.example.gfood.restaurantservice.web;

import com.example.gfood.restaurantservice.domain.RestaurantConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RestaurantConfig.class)
public class RestaurantWebConfig {

}
