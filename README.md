# Saga Choreography (Spring Boot + Kafka) — Multi-Module

🌀 Saga Choreography Flow – E-Commerce Example
1. Order Service

User places order → REST API /orders

Order Service saves order with status = PENDING

Publishes event OrderCreatedEvent → Kafka topic order-events

2. Inventory Service

Listens to order-events

On OrderCreatedEvent:

Check if stock is available

If ✅ → reserve stock → publish InventoryReservedEvent

If ❌ → publish InventoryFailedEvent

3. Payment Service

Listens to inventory-events

On InventoryReservedEvent:

Try charging payment

If ✅ → publish PaymentCompletedEvent

If ❌ → publish PaymentFailedEvent

4. Shipping Service

Listens to payment-events

On PaymentCompletedEvent:

Arrange shipment

Publish OrderShippedEvent

5. Order Service (again)

Listens to multiple events:

On InventoryFailedEvent → update order → CANCELLED

On PaymentFailedEvent → update order → CANCELLED, publish InventoryRollbackEvent

On OrderShippedEvent → update order → COMPLETED

🔄 Compensation (Rollback Flow)

Since Saga Choreography must handle failures:

Inventory fails → Order Service marks order as CANCELLED.

Payment fails →

Order Service cancels order.

Publishes InventoryRollbackEvent → Inventory Service restores stock.

Shipping fails (optional) → Order Service may refund payment + rollback inventory (advanced scenario).

📊 Event Flow Diagram (textual)
User → OrderService → [OrderCreatedEvent] → InventoryService
        ↑                                          ↓
        └── [OrderCancelledEvent] ← [InventoryFailedEvent]

InventoryService → [InventoryReservedEvent] → PaymentService
        ↑                                          ↓
        └── [InventoryRollbackEvent] ← [PaymentFailedEvent]

PaymentService → [PaymentCompletedEvent] → ShippingService
        ↑                                          ↓
        └── [PaymentFailedEvent] → OrderService (cancel)

ShippingService → [OrderShippedEvent] → OrderService (complete)

✅ Final States of Order

COMPLETED → Order placed, inventory reserved, payment done, shipment arranged.

CANCELLED → Any failure in Inventory / Payment / Shipping triggers rollback.
