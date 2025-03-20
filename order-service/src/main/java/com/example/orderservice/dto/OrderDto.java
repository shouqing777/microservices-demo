package com.example.orderservice.dto;

import com.example.orderservice.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    private String orderNumber;

    @NotNull(message = "用戶ID不能為空")
    private Long userId;

    private BigDecimal totalAmount;

    private Order.OrderStatus status;

    private LocalDateTime createdAt;

    @NotEmpty(message = "訂單必須包含至少一個商品")
    @Valid
    private List<OrderItemDto> items = new ArrayList<>();
}