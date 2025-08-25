package com.example.cart.repository;

import com.example.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    // Find all cart items for a user
    List<CartItem> findByUserId(String userId);

    // Find specific cart item by user and product
    Optional<CartItem> findByUserIdAndProductId(String userId, Long productId);
}
