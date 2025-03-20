package com.example.orderservice.client;

import com.example.orderservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id);

    @PatchMapping("/api/products/{id}/stock")
    ResponseEntity<ProductDto> updateProductStock(
            @PathVariable("id") Long id,
            @RequestParam("quantity") Integer quantity);
}