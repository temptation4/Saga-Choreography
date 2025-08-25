package com.example.cart.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InventoryClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public boolean reserveStock(Long productId, int quantity) {
        //http://localhost:8083/product/reservee?id=2&quantity=2
        String url = "http://localhost:8083/product/reservee?id="+productId+"&quantity="+productId;
System.out.println("url "+url);
        System.out.println("quantity "+quantity);
        System.out.println("productId "+productId);
        return restTemplate.getForObject(url, Boolean.class);
    }
}

