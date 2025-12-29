package com.example.deepstacktravel.cart.dto;

import com.example.deepstacktravel.cart.entity.Cart;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CartResponse {
    private Long cartId;
    private Long userId;
    private List<CartItemResponse> items;
    private double totalAmount;

    public static CartResponse fromEntity(Cart cart) {
        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(CartItemResponse::fromEntity)
                .collect(Collectors.toList());

        double totalAmount = items.stream()
                .mapToDouble(CartItemResponse::getSubtotal)
                .sum();

        return CartResponse.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getId())
                .items(items)
                .totalAmount(totalAmount)
                .build();
    }
}
