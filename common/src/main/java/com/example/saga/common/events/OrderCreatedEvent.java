package com.example.saga.common.events;

import java.io.Serializable;

public class OrderCreatedEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private String orderId;
    private double amount;
    private int quantity;
    private String name;

    public OrderCreatedEvent(String orderId, double amount, int quantity, String name) {
        this.orderId = orderId;
        this.amount = amount;
        this.quantity = quantity;
        this.name = name;
    }

    public OrderCreatedEvent() {}

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
