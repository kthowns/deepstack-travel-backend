package com.example.deepstacktravel.cart.controller;

import com.example.deepstacktravel.cart.dto.AddToCartRequest;
import com.example.deepstacktravel.cart.dto.CartResponse;
import com.example.deepstacktravel.cart.service.CartService;
import com.example.deepstacktravel.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal User currentUser) {
        CartResponse cart = cartService.getCart(currentUser.getId());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItemToCart(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody AddToCartRequest request) {
        CartResponse cart = cartService.addItemToCart(currentUser.getId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeItemFromCart(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long cartItemId) {
        cartService.removeItemFromCart(currentUser.getId(), cartItemId);
        return ResponseEntity.noContent().build();
    }
}
