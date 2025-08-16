package com.example.saga.common.events;

public class InventoryCompletedEvent  {  private static final long serialVersionUID = 1L;
        private String orderId;
        private double amount;
        private int quantity;
        public InventoryCompletedEvent() {

        }

    public InventoryCompletedEvent(String orderId, double amount, int quantity) {
        this.orderId = orderId;
        this.amount = amount;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
