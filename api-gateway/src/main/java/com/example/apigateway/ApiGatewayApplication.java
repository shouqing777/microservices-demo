package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // 啟用服務發現客戶端
public class ApiGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
}