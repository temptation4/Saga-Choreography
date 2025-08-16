package com.example.saga.shippingservice.controller;

import com.example.saga.shippingservice.entity.Shipment;
import com.example.saga.shippingservice.repository.ShipmentRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    private final ShipmentRepository repo;

    public ShippingController(ShipmentRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/{orderId}")
    public Shipment getByOrderId(@PathVariable String orderId) {
        return repo.findByOrderId(orderId);
    }
}

