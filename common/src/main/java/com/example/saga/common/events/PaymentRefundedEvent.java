package com.example.saga.common.events;

import java.io.Serializable;

public class PaymentRefundedEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private String orderId;
    private String reason;
    public PaymentRefundedEvent() {}
    public PaymentRefundedEvent(String orderId, String reason) {
        this.orderId = orderId; this.reason = reason;
    }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
