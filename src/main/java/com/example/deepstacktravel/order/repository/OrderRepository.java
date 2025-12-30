package com.example.deepstacktravel.order.repository;

import com.example.deepstacktravel.order.entity.Order;
import com.example.deepstacktravel.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Optional<Order> findByIdAndUser(Long id, User user);
}
