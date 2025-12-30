package com.example.deepstacktravel.order.dto;

import com.example.deepstacktravel.order.entity.Order;
import com.example.deepstacktravel.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponse {
    private Long orderId;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private int totalPrice;

    public static OrderListResponse fromEntity(Order order) {
        return OrderListResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
