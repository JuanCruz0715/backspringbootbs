package com.example.CrudProducts.controllers;

import com.example.CrudProducts.model.Product;
import com.example.CrudProducts.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/products")
@RequiredArgsConstructor

public class ProductController {

    private final ProductService productService;

    // Get products
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getProducts() {return productService.getProducts();}

    // created product
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object>newProducts (@Valid @RequestBody Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            // errores
            List <String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return productService.newProduct(product);
    }

    // refresh product
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateProduct(@PathVariable("id") Long id, @RequestBody Product updatedProduct) {
        return productService.updateProduct(id, updatedProduct);
    }

    // deleted product
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") Long id) {
        return productService.deleteProduct(id);
    }
    // deleted allproduct
    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllProducts() {
        productService.deleteAllProducts();
    }

    // controlador para buscar por id
    @GetMapping("/find/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findProduct(@PathVariable("id") Long id) {
        return productService.findByProduct(id);
    }

}

