package com.example.saga.orderservice.store;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderStore {
    private final Map<String, String> orders = new ConcurrentHashMap<>();
    public void setStatus(String orderId, String status) { orders.put(orderId, status); }
    public String getStatus(String orderId) { return orders.getOrDefault(orderId, "UNKNOWN"); }
}
