package com.example.CrudProducts.service;


import com.example.CrudProducts.model.Product;
import com.example.CrudProducts.repositories.ProductRepository;
import com.example.CrudProducts.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {


    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @BeforeEach
    void setUp (){
        MockitoAnnotations.openMocks(this);
    }


                      //Test-Obtener productos//
    @Test
    public  void testGetProducts() {
        Product product = new Product(12L, "SKU001", "Description1", 100.0, true);
        List<Product> products = Collections.singletonList(product);
        when(productRepository.findAll()).thenReturn(products);
        List<Product> result = productService.getProducts();
        assertEquals(1, result.size());
        assertEquals("SKU001", result.get(0).getSku());
    }


                        //TEST-Eliminar producto//
    @Test
    public void testDeleteProduct(){
        Long productId = 12L;
        Product product = new Product();
        product.setId(productId);

                 //Config. comportamiento repositorio//
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

                 // Llamado método probar//
        ResponseEntity<Object> response = productService.deleteProduct(productId);

                  // Verifica resultado//
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Producto eliminado exitosamente", response.getBody());

                   //Verifica si método deleteById fue llamado correctamente por Id//
        verify(productRepository, times(1)).deleteById(productId);
    }


                            //Test-Crear producto//
    @Test
    public void testNewProduct() {
        // Crear producto ejemplo
        Product product = new Product();
        product.setSku("ABC123");
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setStatus(true); //Cambia a Boolean//

                  //Llamado método a probar//
        ResponseEntity<Object> response = productService.newProduct(product);

                       //Verificar el resultado//
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Producto creado exitosamente", response.getBody());

        //Verifica si método save fue llamado correctamente//
        verify(productRepository, times(1)).save(product);
    }


                     //Test-Actualizar producto//
    @Test
    public void testUpdateProduct() {
        // Creación de producto ejemplo actual
        Long id = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setSku("ABC123");
        existingProduct.setName("Existing Product");
        existingProduct.setPrice(10.0);
        existingProduct.setStatus(true); //Cambia a Boolean//

             //Crea producto ejemplo actualizado//
        Product updatedProduct = new Product();
        updatedProduct.setId(id);
        updatedProduct.setSku("XYZ789");
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(20.0);
        updatedProduct.setStatus(false); // Cambiar a tipo Boolean

              //Simula el comportamiento del repositorio//
        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

                          //Llamar al método a probar//
        ResponseEntity<Object> response = productService.updateProduct(id, updatedProduct);

                          //Verificar el resultado//
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Producto actualizado correctamente", response.getBody());

            //Verifica si el método save fue llamado correctamente//
        verify(productRepository, times(1)).save(existingProduct);

                 //Verifica atributos de producto actualizado//
        assertEquals("XYZ789", existingProduct.getSku());
        assertEquals("Updated Product", existingProduct.getName());
        assertEquals(20.0, existingProduct.getPrice());
        assertEquals(false, existingProduct.getStatus());
    }

}
