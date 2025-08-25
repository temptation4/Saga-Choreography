// CartService.java
package com.example.cart.service;

import com.example.cart.entity.CartItem;
import com.example.cart.repository.CartRepository;
import com.example.cart.client.InventoryClient;
import com.example.cart.config.LoggingThreadPoolExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final InventoryClient inventoryClient;

    private final ExecutorService executor = new LoggingThreadPoolExecutor(5);

    public CartService(CartRepository repo, InventoryClient client) {
        this.cartRepository = repo;
        this.inventoryClient = client;
    }


    // ➕ Add to cart
    public String addToCart(CartItem item) {
        item.setUserId(item.getUserId());
        cartRepository.save(item);
        return "✅ Item " + item.getProductId() + " added to cart for user " + item.getUserId();
    }

    // ➖ Remove item from cart
    public String removeFromCart(String userId, Long productId) {
        CartItem item = cartRepository.findByUserIdAndProductId(userId,productId)
                .orElseThrow(() -> new RuntimeException("❌ Item not found in cart"));
        cartRepository.delete(item);
        return "🗑️ Removed " + productId + " from cart for user " + userId;
    }

    // ✅ Checkout cart (with inventory validation + background tasks)
    public String checkout(String userId) {
        List<CartItem> items = cartRepository.findByUserId(userId);

        if (items.isEmpty()) {
            throw new RuntimeException("❌ Cart is empty for user " + userId);
        }

        for (CartItem item : items) {
            boolean reserved = inventoryClient.reserveStock(item.getProductId(), item.getQuantity());
            if (!reserved) {
                throw new RuntimeException("❌ Product " + item.getProductId() + " out of stock");
            }
        }

        // Run background tasks asynchronously
        executor.submit(() -> sendConfirmationEmail(userId));
        executor.submit(() -> updateAnalytics(userId, items));
        executor.submit(() -> logOrder(userId));

        // Clear cart after successful checkout
        cartRepository.deleteAll(items);

        return "✅ Order placed for user " + userId;
    }
    

    private void sendConfirmationEmail(String userId) {
        if ("badUser".equals(userId)) {
            throw new RuntimeException("SMTP server unavailable!");
        }
        System.out.println("📧 Email sent to user: " + userId);
    }

    private void updateAnalytics(String userId, List<CartItem> items) {
        System.out.println("📊 Analytics updated for user: " + userId + ", items: " + items.size());
    }

    private void logOrder(String userId) {
        System.out.println("📝 Order logged for user: " + userId);
    }

    // 🛒 Get cart items for user
    public List<CartItem> getCart(String userId) {
        return cartRepository.findByUserId(userId);
    }
}
