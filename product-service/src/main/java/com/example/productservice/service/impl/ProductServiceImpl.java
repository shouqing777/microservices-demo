package com.example.productservice.service.impl;

import com.example.productservice.dto.ProductDto;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .category(productDto.getCategory())
                .build();

        Product savedProduct = productRepository.save(product);

        return mapToDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到ID為 " + id + " 的商品"));

        return mapToDto(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByName(String name) {
        return productRepository.findByNameContaining(name).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到ID為 " + id + " 的商品"));

        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStock(productDto.getStock());
        existingProduct.setCategory(productDto.getCategory());

        Product updatedProduct = productRepository.save(existingProduct);

        return mapToDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("找不到ID為 " + id + " 的商品");
        }

        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductDto updateProductStock(Long id, Integer quantity) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到ID為 " + id + " 的商品"));

        existingProduct.setStock(quantity);

        Product updatedProduct = productRepository.save(existingProduct);

        return mapToDto(updatedProduct);
    }

    private ProductDto mapToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .build();
    }
}