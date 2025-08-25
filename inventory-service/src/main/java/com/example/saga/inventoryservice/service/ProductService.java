package com.example.saga.inventoryservice.service;

import com.example.saga.inventoryservice.entity.Product;
import com.example.saga.inventoryservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean reserveStock(Long productId, int quantity) {
        Optional<Product> productOptional = productRepository.findById(productId);

        if (!productOptional.isPresent()) {
            throw new RuntimeException("❌ Product not found: " + productId);
        }

         Product product = productOptional.get();

        if (product.getStock() < quantity) {
            throw new RuntimeException("❌ Not enough stock for: " + productId);
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        return true;
    }


    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
}
