package com.example.productservice.service;

import com.example.productservice.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto getProductById(Long id);

    List<ProductDto> getAllProducts();

    List<ProductDto> getProductsByName(String name);

    List<ProductDto> getProductsByCategory(String category);

    List<ProductDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    ProductDto updateProduct(Long id, ProductDto productDto);

    void deleteProduct(Long id);

    ProductDto updateProductStock(Long id, Integer quantity);
}