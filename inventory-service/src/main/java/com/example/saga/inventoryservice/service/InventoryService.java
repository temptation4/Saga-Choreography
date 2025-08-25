package com.example.saga.inventoryservice.service;

import com.example.saga.inventoryservice.entity.Inventory;
import com.example.saga.inventoryservice.entity.Product;
import com.example.saga.inventoryservice.repository.InventoryRepository;
import com.example.saga.inventoryservice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository repo;

    public InventoryService(InventoryRepository repo) {
        this.repo = repo;
    }

    /**
     * Reserve stock for an order
     */
    @Transactional
    public boolean reserveStock(String productName, int orderQuantity) {
        Inventory inventory = repo.findByName(productName);

        if (inventory == null) {
            System.out.println("❌ Inventory not found for product: " + productName);
            return false;
        }

        if (inventory.getQuantity() >= orderQuantity) {
            inventory.setQuantity(inventory.getQuantity() - orderQuantity); // deduct stock
            inventory.setStatus("RESERVED");
            repo.save(inventory);
            System.out.println("✅ Inventory reserved for product: " + productName);
            return true;
        } else {
            inventory.setStatus("FAILED");
            repo.save(inventory);
            System.out.println("❌ Not enough stock for product: " + productName);
            return false;
        }
    }

    /**
     * Release reserved stock (when order fails later in saga)
     */
    @Transactional
    public void releaseStock(String orderId, int orderQuantity) {
        Inventory inventory = repo.findByOrderId(orderId);

        if (inventory == null) {
            System.out.println("❌ Inventory not found for order: " + orderId);
        }

        // add stock back
        inventory.setQuantity(inventory.getQuantity() + orderQuantity);
        inventory.setStatus("UN-RESERVED");
        repo.save(inventory);

        System.out.println("🔄 Inventory released for order: " + orderId);

    }

    /**
     * Mark stock as failed (final failure)
     */
    @Transactional
    public void failStock(String orderId) {
        Inventory inventory = repo.findByOrderId(orderId);

        if (inventory != null) {
            inventory.setStatus("FAILED");
            repo.save(inventory);
            System.out.println("❌ Inventory marked as FAILED for order: " + orderId);
        }
    }

    public Inventory addInventory(Inventory inventory) {
        return repo.save(inventory);
    }

    public Inventory getByOrderId(String orderId) {
        return repo.findByOrderId(orderId);
    }

    public Inventory getByName(String name) {
        return repo.findByName(name);
    }
}
