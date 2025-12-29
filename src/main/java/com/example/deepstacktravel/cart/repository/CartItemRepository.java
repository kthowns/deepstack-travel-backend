package com.example.deepstacktravel.cart.repository;

import com.example.deepstacktravel.cart.entity.Cart;
import com.example.deepstacktravel.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndId(Cart cart, Long id);
    void deleteByCartAndId(Cart cart, Long id);
    Optional<CartItem> findByCartAndProductId(Cart cart, Long productId);
}
