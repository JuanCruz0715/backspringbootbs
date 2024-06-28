package com.example.CrudOrders.service;

import com.example.CrudOrders.model.Order;
import com.example.CrudOrders.repositories.OrderRepository;
import com.example.CrudOrders.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

                                //Test-obtener OR//
    @Test
    public void testGetOrders() {
        Order order = new Order(1L, 12L, 10.0, 5L, 1000.0, LocalDate.now(), "true");
        List<Order> orders = Collections.singletonList(order);

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getOrders();

        assertEquals(1, result.size());
        assertEquals(12L, result.get(0).getProductId());
    }

                                //Test-eliminar OR//
    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        ResponseEntity<Object> response = orderService.deleteOrder(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Orden eliminada exitosatamente", response.getBody());

        verify(orderRepository, times(1)).deleteById(orderId);
    }

                                //Test-CrearOR//
    @Test
    public void testNewOrder() {
        Order order = new Order(1L, 12L, 10.0, 5L, 1000.0, LocalDate.now(), "true");

        when(orderRepository.save(order)).thenReturn(order);

        ResponseEntity<Object> response = orderService.newOrder(order);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Orden creada exitosamente", response.getBody());
    }

                                 //Test-actualizar OR//
    @Test
    public void testUpdateOrder() {
        Long orderId = 1L;

        Order existingOrder = new Order(1L, 12L, 10.0, 5L, 1000.0, LocalDate.now(), "true");
        Order updatedOrder = new Order(1L, 12L, 20.0, 10L, 2000.0, LocalDate.now(), "false");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(updatedOrder);

        ResponseEntity<Object> response = orderService.updateOrder(orderId, updatedOrder);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Orden actualizada exitosamente", response.getBody());

        assertEquals(updatedOrder.getUnitPrice(), existingOrder.getUnitPrice());
        assertEquals(updatedOrder.getQuantity(), existingOrder.getQuantity());
        assertEquals(updatedOrder.getTotal(), existingOrder.getTotal());
        assertEquals(updatedOrder.getDate(), existingOrder.getDate());
        assertEquals(updatedOrder.getNotes(), existingOrder.getNotes());
    }
}

