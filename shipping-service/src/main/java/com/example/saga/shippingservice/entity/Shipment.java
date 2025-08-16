package com.example.saga.shippingservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderId;

    @Column(name = "status")
    private String status; // SHIPPED

    public Shipment() {}

    public Shipment(String orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    // getters/setters
    public Long getId() { return id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

