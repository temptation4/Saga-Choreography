package com.example.saga.orderservice.listener;

import com.example.saga.common.events.InventoryFailedEvent;
import com.example.saga.common.events.PaymentCompletedEvent;
import com.example.saga.common.events.PaymentFailedEvent;
import com.example.saga.common.events.ShippingCompletedEvent;
import com.example.saga.orderservice.entity.Order;
import com.example.saga.orderservice.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final OrderRepository orderRepository;

    public OrderEventListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "inventory-failed", groupId = "order-service-group")
    public void handleInventoryFailed(InventoryFailedEvent event) {
        System.out.println("OrderService received InventoryFailedEvent: " + event);
        updateOrderStatus(event.getOrderId(), "FAILED");
    }

    @KafkaListener(topics = "payment-failed", groupId = "order-service-group")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        System.out.println("OrderService received PaymentFailedEvent: " + event);
        updateOrderStatus(event.getOrderId(), "FAILED");
    }

    @KafkaListener(topics = "order-shipped", groupId = "order-service-group")
    public void handleShippingCompleted(ShippingCompletedEvent event) {
        System.out.println("OrderService received ShippingCompletedEvent: " + event);
        updateOrderStatus(event.getOrderId(), "SHIPPED");
    }
    @KafkaListener(topics = "payment-completed", groupId = "order-service-group")
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        System.out.println("OrderService received PaymentCompletedEvent: " + event);
        updateOrderStatus(event.getOrderId(), "PAID");
    }

    public void updateOrderStatus(String orderId, String status) {
       Order order = orderRepository.findByOrderId(orderId);
            order.setStatus(status);
            orderRepository.save(order);
    }
}
