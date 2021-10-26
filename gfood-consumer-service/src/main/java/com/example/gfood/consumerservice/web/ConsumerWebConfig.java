package com.example.gfood.consumerservice.web;

import com.example.gfood.consumerservice.domain.ConsumerConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ConsumerConfig.class)
public class ConsumerWebConfig {
}
