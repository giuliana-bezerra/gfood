package com.example.gfood.orderservice.web;

import com.example.gfood.orderservice.domain.OrderConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(OrderConfig.class)
public class OrderWebConfig {

}
