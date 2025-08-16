# Saga Choreography (Spring Boot + Kafka) — Multi-Module

This is a minimal end-to-end example of a **Choreography-based Saga** for an e-commerce order flow:

1. **Order Service** → publishes `OrderCreatedEvent`
2. **Payment Service** → on `OrderCreatedEvent` → approves/declines → `PaymentCompletedEvent` or `PaymentFailedEvent`
3. **Inventory Service** → on `PaymentCompletedEvent` → reserves stock → `StockReservedEvent` or `StockReservationFailedEvent`
4. **Shipping Service** → on `StockReservedEvent` → ships → `OrderShippedEvent`
5. **Compensation** → on `StockReservationFailedEvent` → Payment refunds → `PaymentRefundedEvent` → Order cancels

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

