package com.example.saga.paymentservice.controller;

import com.example.saga.paymentservice.entity.Payment;
import com.example.saga.paymentservice.repository.PaymentRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentRepository repo;
    public PaymentController(PaymentRepository repo){this.repo=repo;}
    @GetMapping("/{orderId}") public Payment get(@PathVariable String orderId){ return repo.findByOrderId(orderId); }
}


