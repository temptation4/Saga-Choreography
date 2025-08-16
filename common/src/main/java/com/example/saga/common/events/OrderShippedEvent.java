package com.example.saga.common.events;

import java.io.Serializable;

public class OrderShippedEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private String orderId;
    public OrderShippedEvent() {}
    public OrderShippedEvent(String orderId) { this.orderId = orderId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
}
