package com.example.deepstacktravel.order.repository;

import com.example.deepstacktravel.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
