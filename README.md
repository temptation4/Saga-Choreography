# Saga Choreography (Spring Boot + Kafka) — Multi-Module

This is a minimal end-to-end example of a **Choreography-based Saga** for an e-commerce order flow:

🔹 Example: E-commerce Order Placement

Flow when a user places an order:

Order Service → creates order with status PENDING and publishes OrderCreatedEvent.

Payment Service → listens for OrderCreatedEvent.

If payment succeeds → publishes PaymentSuccessEvent.

If payment fails → publishes PaymentFailedEvent.

Inventory Service → listens for PaymentSuccessEvent.

Reserves product stock.

If success → publishes InventoryReservedEvent.

If failure → publishes InventoryFailedEvent.

Shipping Service → listens for InventoryReservedEvent and arranges shipment.

If any service fails, they publish a failure event → other services compensate (e.g., Payment refund, release stock, cancel order).

🔹 Architecture Diagram
 ┌────────────┐        ┌─────────────┐        ┌───────────────┐        ┌──────────────┐
 │  Order     │        │  Payment    │        │   Inventory   │        │   Shipping   │
 │  Service   │        │  Service    │        │   Service     │        │   Service    │
 └─────┬──────┘        └─────┬───────┘        └──────┬────────┘        └──────┬──────┘
       │ OrderCreatedEvent    │                   │ PaymentSuccessEvent        │
       └─────────────────────►│                   │                           │
                              │                   └──────────────────────────► │
                              │ PaymentFailedEvent│                           │
                              └──────────────────►│                           │

## Quick Start

### 1) Start Kafka (Docker)
```bash
docker compose up -d
```

### 2) Build all modules
```bash
mvn -q -DskipTests clean package
```

### 3) Start each service in separate terminals
```bash
# Terminal 1
mvn -q -pl order-service -am spring-boot:run

# Terminal 2
mvn -q -pl payment-service -am spring-boot:run

# Terminal 3
mvn -q -pl inventory-service -am spring-boot:run

# Terminal 4
mvn -q -pl shipping-service -am spring-boot:run
```

### 4) Create an order
```bash
curl -X POST "http://localhost:8081/orders?amount=500"
# or try a failing payment:
curl -X POST "http://localhost:8081/orders?amount=1500"
```

### 5) Check order status
```bash
curl "http://localhost:8081/orders/{orderId}"
```

> Default ports:
> - order-service: **8081**
> - payment-service: **8082**
> - inventory-service: **8083**
> - shipping-service: **8084**

Kafka bootstrap: `localhost:9092`

## Topics
- `order-created`
- `payment-completed`
- `payment-failed`
- `stock-reserved`
- `stock-reservation-failed`
- `order-shipped`
- `payment-refunded`
```

