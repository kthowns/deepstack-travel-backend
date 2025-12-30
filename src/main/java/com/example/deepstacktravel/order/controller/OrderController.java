package com.example.deepstacktravel.order.controller;

import com.example.deepstacktravel.order.dto.OrderListResponse;
import com.example.deepstacktravel.order.dto.OrderResponse;
import com.example.deepstacktravel.order.service.OrderService;
import com.example.deepstacktravel.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "현재 장바구니에 담긴 모든 상품으로 새로운 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal User currentUser) {
        OrderResponse order = orderService.createOrder(currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @Operation(summary = "주문 목록 조회", description = "현재 로그인한 사용자의 모든 주문 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<OrderListResponse>> getOrders(@AuthenticationPrincipal User currentUser) {
        List<OrderListResponse> orders = orderService.getOrders(currentUser.getId());
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "주문 상세 조회", description = "특정 주문 번호(orderId)에 해당하는 주문의 상세 정보를 조회합니다.")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetail(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long orderId) {
        OrderResponse orderDetail = orderService.getOrderDetail(currentUser.getId(), orderId);
        return ResponseEntity.ok(orderDetail);
    }

    @Operation(summary = "주문 취소", description = "특정 주문을 취소합니다.")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long orderId) {
        orderService.cancelOrder(currentUser.getId(), orderId);
        return ResponseEntity.noContent().build();
    }
}
