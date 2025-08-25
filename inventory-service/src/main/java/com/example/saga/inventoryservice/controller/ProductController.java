package com.example.saga.inventoryservice.controller;

import com.example.saga.inventoryservice.entity.Product;
import com.example.saga.inventoryservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 🎯 Reserve stock (used by CartService)
    @GetMapping("/reservee")
    public ResponseEntity<Boolean> reserveStock(@RequestParam("id") Long productId,
                                                @RequestParam("quantity") int stock) {
        try {
            boolean reserved = productService.reserveStock(productId, stock);
            return ResponseEntity.ok(reserved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false); // avoid null pointer/500 loops
        }
    }

    // 🎯 Add Product
    @PostMapping("/add/product")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }
}
