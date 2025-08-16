package com.example.saga.common.events;

import java.io.Serializable;

public class PaymentFailedEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private String orderId;
    private int quantity;
    private String reason;
    public PaymentFailedEvent() {}

    public PaymentFailedEvent(String orderId, int quantity, String reason) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.reason = reason;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
