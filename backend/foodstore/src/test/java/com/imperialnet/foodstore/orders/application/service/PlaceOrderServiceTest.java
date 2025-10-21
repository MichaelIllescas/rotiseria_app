package com.imperialnet.foodstore.orders.application.service;

import com.imperialnet.foodstore.orders.application.ports.out.OrderRepositoryPort;
import com.imperialnet.foodstore.orders.domain.model.*;
import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceOrderServiceTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private OrderRepositoryPort orderRepository;

    private PlaceOrderService placeOrderService;

    @BeforeEach
    void setUp() {
        placeOrderService = new PlaceOrderService(productRepository, orderRepository);
    }

    @AfterEach
    void cleanup() {
        MDC.clear();
    }

    @Test
    void deberiaCrearOrdenYDescontarStock() {
        Long productId = 1L;
        Product product = new Product(productId, 2L, "Pizza", "desc", new BigDecimal("100.00"), "img", 5, true);

        when(productRepository.findById(productId)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderItem requestedItem = new OrderItem(productId, 2);
        Order inputOrder = new Order(null,
                "Cliente Test",
                "12345678",
                "cliente@test.com",
                DeliveryType.PICKUP,
                null,
                PaymentMethod.MP,
                null,
                null,
                null,
                Arrays.asList(requestedItem),
                null);

        Order saved = placeOrderService.createOrder(inputOrder);

        // Verificar que se descontó el stock y se persistió el producto
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        Product savedProduct = productCaptor.getValue();
        assertEquals(3, savedProduct.getDailyStock());

        // Verificar que se guardó la orden y que el total fue calculado
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order orderArg = orderCaptor.getValue();

        assertNotNull(saved);
        assertEquals(1, orderArg.getItems().size());
        assertEquals(0, new BigDecimal("200.00").compareTo(orderArg.getTotal()));
        assertEquals(OrderStatus.RECEIVED, orderArg.getStatus());
        assertEquals(PaymentStatus.PENDING, orderArg.getPaymentStatus());
    }

    @Test
    void deberiaLanzarSiProductoNoExiste() {
        Long productId = 99L;
        when(productRepository.findById(productId)).thenReturn(null);

        OrderItem requestedItem = new OrderItem(productId, 1);
        Order inputOrder = new Order(null,
                "Cliente Test",
                "12345678",
                null,
                DeliveryType.PICKUP,
                null,
                PaymentMethod.MP,
                null,
                null,
                null,
                Arrays.asList(requestedItem),
                null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> placeOrderService.createOrder(inputOrder));
        assertTrue(ex.getMessage().contains("Producto no encontrado con ID"));

        verify(productRepository).findById(productId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void deberiaLanzarSiStockInsuficiente() {
        Long productId = 2L;
        Product product = new Product(productId, 2L, "Empanada", "desc", new BigDecimal("50.00"), "img", 1, true);
        when(productRepository.findById(productId)).thenReturn(product);

        OrderItem requestedItem = new OrderItem(productId, 2);
        Order inputOrder = new Order(null,
                "Cliente Test",
                "12345678",
                null,
                DeliveryType.PICKUP,
                null,
                PaymentMethod.MP,
                null,
                null,
                null,
                Arrays.asList(requestedItem),
                null);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> placeOrderService.createOrder(inputOrder));
        assertTrue(ex.getMessage().contains("Stock insuficiente para el producto"));

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }
}

