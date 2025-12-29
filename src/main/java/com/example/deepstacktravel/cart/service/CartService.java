package com.example.deepstacktravel.cart.service;

import com.example.deepstacktravel.cart.dto.CartResponse;
import com.example.deepstacktravel.cart.entity.Cart;
import com.example.deepstacktravel.cart.entity.CartItem;
import com.example.deepstacktravel.cart.repository.CartItemRepository;
import com.example.deepstacktravel.cart.repository.CartRepository;
import com.example.deepstacktravel.product.entity.Product;
import com.example.deepstacktravel.product.repository.ProductRepository;
import com.example.deepstacktravel.user.entity.User;
import com.example.deepstacktravel.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartResponse getCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createCartForUser(user));
        return CartResponse.fromEntity(cart);
    }

    public CartResponse addItemToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createCartForUser(user));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + productId));

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProductId(cart, productId);

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.updateQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = CartItem.createCartItem(cart, product, quantity);
            cartItemRepository.save(newCartItem);
            cart.getCartItems().add(newCartItem); // Ensure the cart's item list is updated
        }

        return CartResponse.fromEntity(cart);
    }

    public void removeItemFromCart(Long userId, Long cartItemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException("Cart not found for user with ID: " + userId));

        CartItem cartItem = cartItemRepository.findByCartAndId(cart, cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found with ID: " + cartItemId + " in user's cart."));

        cart.getCartItems().remove(cartItem); // Remove from the cart's item list
        cartItemRepository.delete(cartItem);
    }

    private Cart createCartForUser(User user) {
        Cart newCart = Cart.createCart(user);
        return cartRepository.save(newCart);
    }
}
