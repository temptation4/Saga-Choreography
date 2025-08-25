package com.example.saga.paymentservice.controller;

import com.example.saga.paymentservice.service.CardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService = new CardService(); // or use @Autowired in real app

    // REST endpoint: place order with card payment
    @PostMapping("/placeOrder")
    public String placeOrder(
            @RequestParam String orderId,
            @RequestParam String cardNumber,
            @RequestParam double amount,
            @RequestParam String customerEmail) {

        cardService.placeOrder(orderId, cardNumber, amount, customerEmail);
        return "✅ Order request accepted for processing (Order ID: " + orderId + ")";
    }

    // optional: to gracefully stop background tasks
    @GetMapping("/shutdown")
    public String shutdown() {
        cardService.shutdown();
        return "CardService background executor shutdown!";
    }
}
