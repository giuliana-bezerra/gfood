package com.example.gfood;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.gfood.consumerservice.main.ConsumerServiceConfig;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootTest(classes = { GfoodEndToEndTests.Config.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class GfoodEndToEndTests extends GfoodServices {
	private String host = "localhost";
	@LocalServerPort
	private int port;

	@Configuration
	@EnableAutoConfiguration
	@ComponentScan
	@Import({ ConsumerServiceConfig.class })
	public static class Config {
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public int getApplicationPort() {
		return port;
	}

	@Test
	public void test() {
		assertEquals("http://localhost:" + port + "/consumers/1", consumerBaseUrl("1"));
	}

	// Add all @Tests here using the operations defined in GfoodEndToEndCommon.
}
