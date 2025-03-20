package com.example.orderservice.service.impl;
import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderItemDto;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        // 生成訂單編號
        String orderNumber = generateOrderNumber();

        // 計算訂單總金額並獲取商品詳情
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemDto itemDto : orderDto.getItems()) {
            // 通過Feign客戶端獲取商品詳情
            ProductDto product = productClient.getProductById(itemDto.getProductId()).getBody();

            if (product == null) {
                throw new EntityNotFoundException("商品ID為 " + itemDto.getProductId() + " 的商品不存在");
            }

            // 檢查庫存
            if (product.getStock() < itemDto.getQuantity()) {
                throw new IllegalStateException("商品 " + product.getName() + " 庫存不足");
            }

            // 更新商品詳情
            itemDto.setProductName(product.getName());
            itemDto.setProductPrice(product.getPrice());
            itemDto.setSubTotal(product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));

            // 更新產品庫存
            int newStock = product.getStock() - itemDto.getQuantity();
            productClient.updateProductStock(product.getId(), newStock);

            // 累計總金額
            totalAmount = totalAmount.add(itemDto.getSubTotal());
        }

        // 建立訂單實體
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .userId(orderDto.getUserId())
                .totalAmount(totalAmount)
                .status(Order.OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        // 建立訂單項目
        List<OrderItem> orderItems = orderDto.getItems().stream()
                .map(itemDto -> OrderItem.builder()
                        .order(order)
                        .productId(itemDto.getProductId())
                        .productName(itemDto.getProductName())
                        .productPrice(itemDto.getProductPrice())
                        .quantity(itemDto.getQuantity())
                        .subTotal(itemDto.getSubTotal())
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);

        // 儲存訂單
        Order savedOrder = orderRepository.save(order);

        return mapToDto(savedOrder);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到ID為 " + id + " 的訂單"));

        return mapToDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到ID為 " + id + " 的訂單"));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        return mapToDto(updatedOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到ID為 " + id + " 的訂單"));

        // 只有待處理或處理中的訂單可以取消
        if (order.getStatus() != Order.OrderStatus.PENDING &&
                order.getStatus() != Order.OrderStatus.PROCESSING) {
            throw new IllegalStateException("只有待處理或處理中的訂單可以取消");
        }

        // 恢復庫存
        for (OrderItem item : order.getItems()) {
            ProductDto product = productClient.getProductById(item.getProductId()).getBody();

            if (product != null) {
                int newStock = product.getStock() + item.getQuantity();
                productClient.updateProductStock(product.getId(), newStock);
            }
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private String generateOrderNumber() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10).toUpperCase();
    }

    private OrderDto mapToDto(Order order) {
        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(item -> OrderItemDto.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .productPrice(item.getProductPrice())
                        .quantity(item.getQuantity())
                        .subTotal(item.getSubTotal())
                        .build())
                .collect(Collectors.toList());

        return OrderDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(itemDtos)
                .build();
    }
}