# Saga Design Pattern - Choreography

This project demonstrates the **Saga Choreography pattern** using Spring Boot and Kafka.  
It simulates an **Order workflow** across multiple microservices with event-driven communication.

---

## 🚀 Microservices in the Project

1. **Order Service**
   - Accepts order requests from clients.
   - Creates orders in `PENDING` state.
   - Publishes **`OrderCreatedEvent`**.

2. **Inventory Service**
   - Consumes **`OrderCreatedEvent`**.
   - Reserves product stock if available.
   - Publishes:
     - **`InventoryReservedEvent`** (success)  
     - **`InventoryFailedEvent`** (failure)

3. **Payment Service**
   - Consumes **`InventoryReservedEvent`**.
   - Processes payment.
   - Publishes:
     - **`PaymentCompletedEvent`** (success)  
     - **`PaymentFailedEvent`** (failure → triggers inventory release)

4. **Shipping Service**
   - Consumes **`PaymentCompletedEvent`**.
   - Prepares shipping.
   - Publishes **`OrderShippedEvent`**.

5. **Order Service Compensation**
   - Consumes:
     - **`InventoryFailedEvent`** → updates order as `CANCELLED`
     - **`PaymentFailedEvent`** → updates order as `CANCELLED`
     - **`OrderShippedEvent`** → updates order as `COMPLETED`

---

## 🛠 Event Flow

1. **Client → Order Service**
   - API call: `POST /orders`
   - Order status = `PENDING`
   - Publishes **OrderCreatedEvent**

2. **OrderCreatedEvent → Inventory Service**
   - If stock available → Publishes **InventoryReservedEvent**  
   - If stock not available → Publishes **InventoryFailedEvent**

3. **InventoryReservedEvent → Payment Service**
   - If payment successful → Publishes **PaymentCompletedEvent**  
   - If payment failed → Publishes **PaymentFailedEvent**

4. **PaymentCompletedEvent → Shipping Service**
   - Shipping prepared → Publishes **OrderShippedEvent**

5. **Failure Handling**
   - `InventoryFailedEvent` → Order Service updates status to `CANCELLED`
   - `PaymentFailedEvent` → Order Service updates status to `CANCELLED` and Inventory Service releases stock

6. **Success Handling**
   - `OrderShippedEvent` → Order Service updates status to `COMPLETED`

---
<img width="1398" height="462" alt="image" src="https://github.com/user-attachments/assets/a968b09e-459f-4846-9e72-397d8fbdfbcd" />

## 📌 Topics Used in Kafka

- `order-created`
- `inventory-reserved`
- `inventory-failed`
- `payment-completed`
- `payment-failed`
- `order-shipped`

---

## ✅ Final Outcome

- **Happy Path** → Order moves through all services → status becomes **`COMPLETED`**.  
- **Failure Path** → Any failure (Inventory or Payment) → order becomes **`CANCELLED`**.

---

## ⚡ Tech Stack

- Java 19
- Spring Boot 3
- Spring Kafka
- MySQL (Order DB)
- Docker / Docker Compose
- Apache Kafka & Zookeeper

---

## 🔑 Running the Project

1. Start Kafka & Zookeeper using Docker.
2. Run all microservices (`Order`, `Inventory`, `Payment`, `Shipping`).
3. Place an order via **Order Service API**.
4. Observe the **choreographed events** in Kafka topics.


---

## 👨‍💻 Author
Neelu Sahai  
