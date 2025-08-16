package com.example.saga.orderservice.dto;

public record OrderResponseDTO(
        String orderId,
        String status,
        String message
) {}

