package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private Long id;

    @NotNull(message = "商品ID不能為空")
    private Long productId;

    private String productName;

    private BigDecimal productPrice;

    @NotNull(message = "數量不能為空")
    @Min(value = 1, message = "數量必須大於0")
    private Integer quantity;

    private BigDecimal subTotal;
}