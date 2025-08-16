package com.example.saga.orderservice.dto;

public record OrderRequestDTO(
        double amount,
        int quantity,
        String name
) {}

