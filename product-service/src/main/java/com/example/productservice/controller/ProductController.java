package com.example.productservice.controller;

import com.example.productservice.dto.ProductDto;
import com.example.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> getProductsByName(@RequestParam String name) {
        List<ProductDto> products = productService.getProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable String category) {
        List<ProductDto> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ProductDto>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<ProductDto> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductDto> updateProductStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        ProductDto updatedProduct = productService.updateProductStock(id, quantity);
        return ResponseEntity.ok(updatedProduct);
    }
}