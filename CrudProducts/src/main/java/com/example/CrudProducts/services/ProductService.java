package com.example.CrudProducts.services;

import com.example.CrudProducts.model.Product;
import com.example.CrudProducts.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ProductService {

    private final ProductRepository productRepository;

    // get products
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    // created products
    public ResponseEntity<Object> newProduct(Product product) {
        productRepository.save(product);
        return new ResponseEntity<>("Product created ", HttpStatus.CREATED);
    }

    // deleted products
    public ResponseEntity<Object> deleteProduct(Long id) {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isPresent()) {
            productRepository.deleteById(id);
            return new ResponseEntity<>("Product deleted ", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }
    //
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    // update product
    public ResponseEntity<Object> updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            existingProduct.setSku(updatedProduct.getSku());
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStatus(updatedProduct.getStatus());

            productRepository.save(existingProduct);

            return new ResponseEntity<>("Product updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    /* Find id product*/
    public ResponseEntity<Object> findByProduct(Long id) {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            return new ResponseEntity<>(existingProduct, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }
}


