package com.example.gfood;

import com.example.gfood.consumerservice.main.ConsumerServiceConfig;
import com.example.gfood.courierservice.main.CourierServiceConfig;
import com.example.gfood.orderservice.main.OrderServiceConfig;
import com.example.gfood.restaurantservice.main.RestaurantServiceConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan
@Import({ ConsumerServiceConfig.class, RestaurantServiceConfig.class, CourierServiceConfig.class,
		OrderServiceConfig.class })
public class GfoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(GfoodApplication.class, args);
	}

}
