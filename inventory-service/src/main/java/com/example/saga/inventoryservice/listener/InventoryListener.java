package com.example.saga.inventoryservice.listener;

import com.example.saga.common.events.*;
import com.example.saga.inventoryservice.service.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class InventoryListener {

    private final InventoryService service;
    private final KafkaTemplate<String, Object> kafka;

    public InventoryListener(InventoryService service, KafkaTemplate<String, Object> kafka) {
        this.service = service;
        this.kafka = kafka;
    }

    @KafkaListener(topics = "order-created", groupId = "inventory-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void onOrderCreated(@Payload OrderCreatedEvent event) {
        System.out.println("📦 InventoryListener received order: " + event);

        boolean success = service.reserveStock(event.getName(), event.getQuantity());

        if (success) {
            System.out.println("✅ Stock available for : " + event.getOrderId());
            kafka.send("inventory-completed",
                    new InventoryCompletedEvent(event.getOrderId(), event.getAmount(),event.getQuantity()));
        } else {
            System.out.println("❌ Stock not available for order: " + event.getOrderId());
            kafka.send("inventory-failed",
                    new InventoryFailedEvent(event.getOrderId(), "Stock not available"));
        }
    }

    @KafkaListener(topics = "payment-failed", groupId = "inventory-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void onPaymentFailed(@Payload PaymentFailedEvent event) {
        System.out.println("⚠️ Payment failed for order: " + event.getOrderId() + " -> releasing stock");
        // TODO: Implement refund/release stock logic
        service.releaseStock(event.getOrderId(),event.getQuantity());
    }
}
