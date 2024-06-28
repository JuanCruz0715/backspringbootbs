package com.example.CrudOrders.services;

import com.example.CrudOrders.model.Order;
import com.example.CrudOrders.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class OrderService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final OrderRepository orderRepository;

                              //Obtener OR//
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

                              //Crear OR/
    public ResponseEntity<Object> createOrder(Order order) {
        Long productId = order.getProductId();
        String url = "http://localhost:8080/api/products/find/" + productId;
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                        //guarda OR si encuentra prod//
                orderRepository.save(order);
                return new ResponseEntity<>("Order created successfully", HttpStatus.CREATED);
            } else {
                //no guarda OR si no encuentra prod//
                return new ResponseEntity<>("Product not found, order not created", HttpStatus.NOT_FOUND);
            }
        } catch (HttpClientErrorException.NotFound ex) {
            return new ResponseEntity<>("Product not found, order not created", HttpStatus.NOT_FOUND);
        }
    }

                                    //Eliminar OR//
    public ResponseEntity<Object> deleteOrder(Long id) {
        Optional<Order> existingOrderOptional = orderRepository.findById(id);
        if (existingOrderOptional.isPresent()) {
            orderRepository.deleteById(id);
            return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }

                                   //ACTUALIZAR OR//
    public ResponseEntity<Object> updateOrder(Long id, Order updatedOrder) {
        Optional<Order> existingOrderOptional = orderRepository.findById(id);
        if (existingOrderOptional.isPresent()) {
            Order existingOrder = existingOrderOptional.get();
            existingOrder.setProductId(updatedOrder.getProductId());
            existingOrder.setUnitPrice(updatedOrder.getUnitPrice());
            existingOrder.setQuantity(updatedOrder.getQuantity());
            existingOrder.setTotal(updatedOrder.getTotal());
            existingOrder.setDate(updatedOrder.getDate());
            existingOrder.setNotes(updatedOrder.getNotes());

            orderRepository.save(existingOrder);

            return new ResponseEntity<>("Order updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }
}
