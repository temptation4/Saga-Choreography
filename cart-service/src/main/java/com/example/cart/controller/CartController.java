package com.example.cart.controller;

import com.example.cart.entity.CartItem;
import com.example.cart.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService service) {
        this.cartService = service;
    }

    // 🛒 Get all items in cart
    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable String userId) {
        return cartService.getCart(userId);
    }

    // ➕ Add item to cart
    @PostMapping("/add")
    public String addToCart(@RequestBody CartItem item) {
        return cartService.addToCart(item);
    }

    // ➖ Remove item from cart
    @DeleteMapping("/{userId}/remove/{productId}")
    public String removeFromCart(@PathVariable String userId, @PathVariable Long productId) {
        return cartService.removeFromCart(userId, productId);
    }

    // ✅ Checkout cart
    @GetMapping("/{userId}/checkout")
    public String checkout(@PathVariable String userId) {
        return cartService.checkout(userId);
    }
}
