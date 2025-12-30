package com.example.deepstacktravel.cart.controller;

import com.example.deepstacktravel.cart.dto.AddToCartRequest;
import com.example.deepstacktravel.cart.dto.CartResponse;
import com.example.deepstacktravel.cart.service.CartService;
import com.example.deepstacktravel.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니 조회", description = "현재 로그인한 사용자의 장바구니 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal User currentUser) {
        CartResponse cart = cartService.getCart(currentUser.getId());
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "장바구니에 상품 추가", description = "장바구니에 새로운 상품을 추가하거나 기존 상품의 수량을 업데이트합니다.")
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItemToCart(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody AddToCartRequest request) {
        CartResponse cart = cartService.addItemToCart(currentUser.getId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "장바구니 상품 삭제", description = "장바구니에서 특정 상품을 삭제합니다.")
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeItemFromCart(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long cartItemId) {
        cartService.removeItemFromCart(currentUser.getId(), cartItemId);
        return ResponseEntity.noContent().build();
    }
}
