package com.example.saga.orderservice.controller;

import com.example.saga.orderservice.dto.OrderRequestDTO;
import com.example.saga.orderservice.dto.OrderResponseDTO;
import com.example.saga.orderservice.entity.Order;
import com.example.saga.orderservice.kafka.OrderProducer;
import com.example.saga.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderProducer producer;

    public OrderController(OrderService orderService, OrderProducer producer) {
        this.orderService = orderService;
        this.producer = producer;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO request) {
        OrderResponseDTO response = orderService.createOrder(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public Order getOrderByOrderId(@PathVariable String orderId){ return orderService.getOrderByOrderId(orderId); }

    @GetMapping("getAllOrders")
    public List<Order> getOrders(){ return orderService.getAllOrders(); }
}
