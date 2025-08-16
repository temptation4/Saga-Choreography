# 🌀 Saga Design Pattern (Choreography) – Microservices Example

This project demonstrates the **Saga Choreography Pattern** using **Spring Boot, Kafka, and MySQL** across multiple microservices.  
The goal is to manage **distributed transactions** in an e-commerce order workflow (Order → Payment → Inventory → Shipping).

---

## 📌 Services in the Project

1. **Order Service**
   - Receives order requests.
   - Publishes `OrderCreatedEvent` to Kafka.
   - Updates order status based on events from other services.

2. **Payment Service**
   - Listens for `OrderCreatedEvent`.
   - Tries to process the payment.
   - Publishes either `PaymentSuccessEvent` or `PaymentFailedEvent`.

3. **Inventory Service**
   - Listens for `PaymentSuccessEvent`.
   - Validates product availability.
   - Publishes either `InventoryReservedEvent` or `InventoryFailedEvent`.

4. **Shipping Service**
   - Listens for `InventoryReservedEvent`.
   - Schedules shipment for successful orders.
   - Publishes `OrderShippedEvent`.

---

## 🔄 Complete Flow of the Saga (Choreography)

### ✅ Success Flow
Customer -> Order Service -> Kafka -> Payment Service -> Inventory Service -> Shipping Service

markdown
Copy
Edit

1. **Customer places an order** (Order Service creates record → status = `PENDING`).  
2. `OrderCreatedEvent` is published.  
3. **Payment Service** processes payment → publishes `PaymentSuccessEvent`.  
4. **Inventory Service** reserves items → publishes `InventoryReservedEvent`.  
5. **Shipping Service** ships order → publishes `OrderShippedEvent`.  
6. **Order Service** updates order status to **COMPLETED**.  

---

### ❌ Failure Flows

#### Case 1: Payment Failure
1. Payment Service fails → publishes `PaymentFailedEvent`.  
2. Order Service updates order → status = **CANCELLED**.  

#### Case 2: Inventory Failure
1. Inventory Service fails → publishes `InventoryFailedEvent`.  
2. Order Service updates order → status = **CANCELLED**.  
3. Payment Service may trigger **compensation** (refund).  

#### Case 3: Shipping Failure
1. Shipping fails → publishes `ShippingFailedEvent`.  
2. Order Service updates order → status = **FAILED**.  
3. Compensation may trigger (release inventory, refund payment).  

---

## 🗂️ Project Structure

Saga-Choreography/
│── order-service/
│── payment-service/
│── inventory-service/
│── shipping-service/
│── common/ # Shared event classes and DTOs
│── docker-compose.yml
│── README.md

yaml
Copy
Edit

---

## ⚙️ Tech Stack

- **Spring Boot 3**
- **Kafka** (event streaming)
- **MySQL** (persistent storage)
- **JPA / Hibernate**
- **Docker & Docker Compose** (for containerized deployment)

---

## ▶️ How to Run

### 1. Start Kafka & Zookeeper
```bash
docker-compose up -d
2. Run Each Service
Inside each service folder:

bash
Copy
Edit
mvn spring-boot:run
3. Place an Order
http
Copy
Edit
POST http://localhost:8088/orders
Content-Type: application/json

{
  "customerId": 1,
  "productId": 101,
  "amount": 500
}
4. Monitor Flow
Kafka Topics: order-created, payment-success, payment-failed, inventory-reserved, inventory-failed, order-shipped

Check orderdb table to verify order status.

📊 Order Status Lifecycle
Event	Order Status
Order Created	PENDING
Payment Success	PAYMENT_DONE
Inventory Reserved	INVENTORY_OK
Shipping Success	COMPLETED
Payment/Inventory Fail	CANCELLED
Shipping Fail	FAILED

🚀 Future Improvements
Add Dead Letter Queues (DLQ) for failed events.

Implement Resilience4j for retries and circuit breakers.

Add Monitoring (Prometheus + Grafana).

👨‍💻 Author
Neelu Sahai – Senior Java Developer (11 years experience)
📧 Email: neelhuma@gmail.com
🔗 GitHub: temptation4

