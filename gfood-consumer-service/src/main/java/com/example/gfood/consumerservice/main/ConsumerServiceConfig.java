package com.example.gfood.consumerservice.main;

import com.example.gfood.consumerservice.web.ConsumerWebConfig;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ConsumerWebConfig.class)
public class ConsumerServiceConfig {
}
