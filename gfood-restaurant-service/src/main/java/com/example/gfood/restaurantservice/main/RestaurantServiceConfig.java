package com.example.gfood.restaurantservice.main;

import com.example.gfood.restaurantservice.web.RestaurantWebConfig;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RestaurantWebConfig.class)
public class RestaurantServiceConfig {

}
