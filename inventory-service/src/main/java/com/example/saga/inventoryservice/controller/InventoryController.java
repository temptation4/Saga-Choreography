package com.example.saga.inventoryservice.controller;
import com.example.saga.inventoryservice.entity.Inventory;
import com.example.saga.inventoryservice.repository.InventoryRepository;
import com.example.saga.inventoryservice.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{orderId}")
    public Inventory getByOrderId(@PathVariable String orderId) {
        return inventoryService.getByOrderId(orderId);
    }

    @GetMapping("/{name}")
    public Inventory getByName(@PathVariable String name) {
        return inventoryService.getByName(name);
    }

    @PostMapping("/add")
    public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory) {
        Inventory savedInventory = inventoryService.addInventory(inventory);
        return ResponseEntity.ok(savedInventory);
    }
}

