package com.example.gfood.courierservice.web;

import com.example.gfood.courierservice.domain.CourierConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CourierConfig.class)
public class CourierWebConfig {

}
