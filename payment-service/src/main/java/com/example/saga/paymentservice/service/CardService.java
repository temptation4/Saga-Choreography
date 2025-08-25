package com.example.saga.paymentservice.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CardService {
    private final ExecutorService backgroundExecutor = Executors.newFixedThreadPool(4);

    // Critical: Charge the card synchronously
    public boolean chargeCard(String cardNumber, double amount) {
        System.out.println("💳 Charging card: " + cardNumber + " for amount $" + amount);
        // Fake payment logic
        try {
            Thread.sleep(1000); // simulate payment gateway delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("✅ Payment successful");
        return true;
    }

    // Place an order: synchronous + async background
    public void placeOrder(String orderId, String cardNumber, double amount, String customerEmail) {
        // Step 1: Critical work (charge card)
        boolean paid = chargeCard(cardNumber, amount);

        if (paid) {
            System.out.println("📦 Order " + orderId + " confirmed!");

            // Step 2: Background tasks
            backgroundExecutor.submit(() -> sendEmail(customerEmail, orderId));
            backgroundExecutor.submit(() -> updateInventory(orderId));
            backgroundExecutor.submit(() -> notifyWarehouse(orderId));
            backgroundExecutor.submit(() -> updateRecommendationEngine(orderId));

            System.out.println("User sees: ✅ Order placed successfully!");
        } else {
            System.out.println("❌ Payment failed for " + orderId);
        }
    }

    private void sendEmail(String email, String orderId) {
        System.out.println("📧 Sending confirmation email for " + orderId + " to " + email);
    }

    private void updateInventory(String orderId) {
        System.out.println("📦 Updating inventory for " + orderId);
    }

    private void notifyWarehouse(String orderId) {
        System.out.println("🏭 Notifying warehouse to ship " + orderId);
    }

    private void updateRecommendationEngine(String orderId) {
        System.out.println("🤖 Updating recommendation engine with " + orderId);
    }

    public void shutdown() {
        backgroundExecutor.shutdown();
    }
}

