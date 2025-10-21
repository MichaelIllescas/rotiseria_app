package com.imperialnet.foodstore.orders.application.service;

import com.imperialnet.foodstore.orders.application.ports.in.CreateOrderUseCase;
import com.imperialnet.foodstore.orders.application.ports.out.OrderRepositoryPort;
import com.imperialnet.foodstore.orders.domain.model.*;
import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Caso de uso: crear un nuevo pedido.
 * Completa los datos de los productos, descuenta el stock y persiste la orden.
 */
@Service
public class PlaceOrderService implements CreateOrderUseCase {

    private final ProductRepositoryPort productRepository;
    private final OrderRepositoryPort orderRepository;

    public PlaceOrderService(ProductRepositoryPort productRepository,
                             OrderRepositoryPort orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public Order createOrder(Order inputOrder) {

        // ============================
        // 1️⃣ Completar información y validar stock
        // ============================
        List<OrderItem> completedItems = new ArrayList<>();

        for (OrderItem item : inputOrder.getItems()) {
            Product product = productRepository.findById(item.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Producto no encontrado con ID: " + item.getProductId());
            }

            // ⚠️ Verificar stock disponible
            if (product.getDailyStock() < item.getQuantity()) {
                throw new IllegalStateException(
                        "Stock insuficiente para el producto: " + product.getName() +
                                " (disponible: " + product.getDailyStock() + ", solicitado: " + item.getQuantity() + ")"
                );
            }

            // ⚙️ Descontar stock directamente en el producto
            int nuevoStock = product.getDailyStock() - item.getQuantity();
            product.resetDailyStock(nuevoStock);

            // 💾 Actualizar en repositorio de productos
            productRepository.save(product);

            // 🧱 Crear el OrderItem completo
            OrderItem orderItem = new OrderItem(item.getProductId(), item.getQuantity());
            orderItem.completeProductData(product.getName(), product.getPrice());
            completedItems.add(orderItem);
        }

        // ============================
        // 2️⃣ Crear la nueva Order (con todos los campos requeridos)
        // ============================
        Order newOrder = new Order(
                null,                                // id generado por JPA
                inputOrder.getCustomerName(),
                inputOrder.getPhone(),
                inputOrder.getEmail(),
                inputOrder.getDeliveryType(),
                inputOrder.getAddress(),
                inputOrder.getPaymentMethod(),
                OrderStatus.RECEIVED,                // estado inicial
                PaymentStatus.PENDING,               // estado de pago inicial
                BigDecimal.ZERO,                     // total se recalcula
                new ArrayList<>(),                   // comenzamos con lista vacía
                LocalDateTime.now()
        );

        // 🧮 Agregar los ítems (esto recalcula el total)
        completedItems.forEach(newOrder::addItem);

        // ============================
        // 3️⃣ Guardar la orden
        // ============================
        return orderRepository.save(newOrder);
    }
}
