package com.example.saga.inventoryservice.repository;

import com.example.saga.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByOrderId(String orderId);

    Inventory findByName(String name);

}

