package com.example.saga.orderservice.service;

import com.example.saga.common.events.OrderCreatedEvent;
import com.example.saga.orderservice.dto.OrderRequestDTO;
import com.example.saga.orderservice.dto.OrderResponseDTO;
import com.example.saga.orderservice.entity.Order;
import com.example.saga.orderservice.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    // Create Order
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        Order order = new Order();
        order.setOrderId(generateUniqueString());
        order.setAmount(request.amount());
        order.setName(request.name());
        order.setQuantity(request.quantity());
        order.setStatus("PENDING");

        orderRepository.save(order);

        // Publish OrderCreated event
        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getOrderId(), order.getAmount(), order.getQuantity(),order.getName()
        );
        System.out.println("Publish OrderCreated event");
        kafkaTemplate.send("order-created", event);

        return new OrderResponseDTO(order.getOrderId(), order.getStatus(), "Order is being processed");
    }

    // Method to generate unique string: 5 letters + 5 digits
    private String generateUniqueString() {
        StringBuilder sb = new StringBuilder();

        // Generate 5 random uppercase letters
        for (int i = 0; i < 5; i++) {
            char letter = (char) ('A' + ThreadLocalRandom.current().nextInt(0, 26));
            sb.append(letter);
        }

        // Generate 5 random digits
        for (int i = 0; i < 5; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(0, 10));
        }

        return sb.toString();
    }

    public Order getOrderByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(String orderId, String status){
        Order order = orderRepository.findByOrderId(orderId);
        if(order != null){
            order.setStatus(status);
            orderRepository.save(order);
        }
    }


}

