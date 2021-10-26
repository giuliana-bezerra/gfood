package com.example.gfood;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest
class GfoodApplicationTests extends GfoodEndToEndTests {
	@LocalServerPort
	private int port;

	@Test
	void contextLoads() {
	}

	@Override
	public String getHost() {
		return "localhost";
	}

	@Override
	public int getApplicationPort() {
		return port;
	}
}
