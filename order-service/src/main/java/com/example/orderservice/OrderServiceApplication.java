package com.example.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient  // 啟用服務發現客戶端
@EnableFeignClients     // 啟用 Feign 客戶端
public class OrderServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}
}