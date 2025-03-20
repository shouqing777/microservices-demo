// 在 order-service/src/main/java/com/example/orderservice/service/OrderService.java
package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.Order;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);

    OrderDto getOrderById(Long id);

    List<OrderDto> getAllOrders();

    List<OrderDto> getOrdersByUserId(Long userId);

    List<OrderDto> getOrdersByStatus(Order.OrderStatus status);

    OrderDto updateOrderStatus(Long id, Order.OrderStatus status);

    void cancelOrder(Long id);
}