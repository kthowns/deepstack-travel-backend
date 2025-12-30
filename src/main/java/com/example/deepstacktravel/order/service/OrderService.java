package com.example.deepstacktravel.order.service;

import com.example.deepstacktravel.cart.entity.Cart;
import com.example.deepstacktravel.cart.repository.CartRepository;
import com.example.deepstacktravel.order.dto.OrderListResponse;
import com.example.deepstacktravel.order.dto.OrderResponse;
import com.example.deepstacktravel.order.entity.Order;
import com.example.deepstacktravel.order.entity.OrderItem;
import com.example.deepstacktravel.order.repository.OrderRepository;
import com.example.deepstacktravel.user.entity.User;
import com.example.deepstacktravel.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public OrderResponse createOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException("Cart not found for user with ID: " + userId));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot create an order.");
        }

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> OrderItem.createOrderItem(cartItem.getProduct(), cartItem.getQuantity()))
                .collect(Collectors.toList());

        Order order = Order.createOrder(user, orderItems);
        orderRepository.save(order);

        // Clear the cart after order creation
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return OrderResponse.fromEntity(order);
    }

    @Transactional(readOnly = true)
    public List<OrderListResponse> getOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(OrderListResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderDetail(Long userId, Long orderId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + orderId + " for this user."));
        return OrderResponse.fromEntity(order);
    }

    public void cancelOrder(Long userId, Long orderId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + orderId + " for this user."));
        
        order.cancel();
        // The @Transactional annotation will ensure the state change is persisted.
    }
}
