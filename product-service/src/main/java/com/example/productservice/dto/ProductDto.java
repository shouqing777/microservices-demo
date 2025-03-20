package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank(message = "商品名稱不能為空")
    private String name;

    private String description;

    @NotNull(message = "商品價格不能為空")
    @Min(value = 0, message = "商品價格不能為負數")
    private BigDecimal price;

    @NotNull(message = "庫存數量不能為空")
    @Min(value = 0, message = "庫存數量不能為負數")
    private Integer stock;

    private String category;
}