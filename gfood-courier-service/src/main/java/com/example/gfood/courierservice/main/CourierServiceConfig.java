package com.example.gfood.courierservice.main;

import com.example.gfood.courierservice.web.CourierWebConfig;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CourierWebConfig.class)
public class CourierServiceConfig {

}
