package com.example.gfood.orderservice.main;

import com.example.gfood.orderservice.web.OrderWebConfig;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(OrderWebConfig.class)
public class OrderServiceConfig {

}
