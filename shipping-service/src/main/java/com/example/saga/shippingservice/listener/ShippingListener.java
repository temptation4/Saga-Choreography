package com.example.saga.shippingservice.listener;

import com.example.saga.common.events.PaymentCompletedEvent;
import com.example.saga.common.events.OrderShippedEvent;
import com.example.saga.common.events.ShippingCompletedEvent;
import com.example.saga.shippingservice.entity.Shipment;
import com.example.saga.shippingservice.repository.ShipmentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShippingListener {

    private final ShipmentRepository repo;
    private final KafkaTemplate<String, Object> kafka;

    public ShippingListener(ShipmentRepository repo, KafkaTemplate<String, Object> kafka) {
        this.repo = repo;
        this.kafka = kafka;
    }

    @KafkaListener(topics = "payment-completed", groupId = "shipping-service-group")
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        // Save shipment
        repo.save(new Shipment(event.getOrderId(), "SHIPPED"));

        // Publish event to "order-shipped"
        kafka.send("order-shipped", new ShippingCompletedEvent(event.getOrderId()));
    }
}
