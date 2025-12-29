package com.example.deepstacktravel.cart.repository;

import com.example.deepstacktravel.cart.entity.Cart;
import com.example.deepstacktravel.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
