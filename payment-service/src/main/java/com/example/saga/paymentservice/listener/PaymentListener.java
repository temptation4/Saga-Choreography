package com.example.saga.paymentservice.listener;

import com.example.saga.common.events.InventoryCompletedEvent;
import com.example.saga.common.events.PaymentCompletedEvent;
import com.example.saga.common.events.PaymentFailedEvent;
import com.example.saga.paymentservice.entity.Payment;
import com.example.saga.paymentservice.repository.PaymentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PaymentRepository paymentRepository;

    public PaymentListener(KafkaTemplate<String, Object> kafkaTemplate,
                           PaymentRepository paymentRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.paymentRepository = paymentRepository;
    }

    @KafkaListener(
            topics = "inventory-completed",
            groupId = "payment-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleInventoryCompleted(InventoryCompletedEvent event) {
        System.out.println("✅ Payment Service received inventory-completed event: " + event);

        try {
            if (event.getAmount() < 1000) {
                // save SUCCESS payment
                Payment payment = new Payment(event.getOrderId(), event.getAmount(), "SUCCESS");
                paymentRepository.save(payment);

                PaymentCompletedEvent paymentProcessed = new PaymentCompletedEvent(event.getOrderId());
                kafkaTemplate.send("payment-completed", paymentProcessed);
                System.out.println("✅ Payment processed and saved: " + paymentProcessed);
            } else {
                // save FAILED payment
                Payment payment = new Payment(event.getOrderId(), event.getAmount(), "Failed");
                paymentRepository.save(payment);

                PaymentFailedEvent paymentFailed = new PaymentFailedEvent(event.getOrderId(),event.getQuantity() ,"Amount too high!");
                kafkaTemplate.send("payment-failed", paymentFailed);
                System.out.println("❌ Payment failed and saved: " + paymentFailed);
            }
        } catch (Exception e) {
            // save FAILED payment with exception reason
            Payment payment = new Payment(event.getOrderId(), event.getAmount(), e.getMessage());
            paymentRepository.save(payment);

            PaymentFailedEvent paymentFailed = new PaymentFailedEvent(event.getOrderId(),event.getQuantity(), e.getMessage());
            kafkaTemplate.send("payment-failed", paymentFailed);
            System.out.println("❌ Payment failed (exception) and saved: " + paymentFailed);
        }
    }
}
