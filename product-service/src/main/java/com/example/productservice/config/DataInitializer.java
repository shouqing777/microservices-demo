package com.example.productservice.config;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Autowired
    private ProductRepository productRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (productRepository.count() == 0) {
                Product product1 = Product.builder()
                        .name("iPhone 15")
                        .description("最新款 iPhone，支援 5G 網路")
                        .price(new BigDecimal("32000"))
                        .stock(100)
                        .category("電子產品")
                        .build();

                Product product2 = Product.builder()
                        .name("MacBook Pro")
                        .description("13 吋，Apple M3 晶片，16GB 記憶體")
                        .price(new BigDecimal("59000"))
                        .stock(50)
                        .category("電子產品")
                        .build();

                Product product3 = Product.builder()
                        .name("AirPods Pro")
                        .description("主動降噪功能，無線充電")
                        .price(new BigDecimal("8000"))
                        .stock(200)
                        .category("配件")
                        .build();

                productRepository.saveAll(Arrays.asList(product1, product2, product3));

                System.out.println("初始商品資料已載入到資料庫!");
            }
        };
    }
}