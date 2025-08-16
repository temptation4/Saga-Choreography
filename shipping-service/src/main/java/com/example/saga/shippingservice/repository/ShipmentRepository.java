package com.example.saga.shippingservice.repository;

import com.example.saga.shippingservice.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Shipment findByOrderId(String orderId);
}

