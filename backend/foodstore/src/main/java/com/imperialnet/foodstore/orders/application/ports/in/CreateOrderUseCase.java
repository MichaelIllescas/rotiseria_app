package com.imperialnet.foodstore.orders.application.ports.in;

import com.imperialnet.foodstore.orders.domain.model.Order;

/**
 * Caso de uso principal para crear una nueva orden.
 * Define el contrato que la capa de aplicación debe cumplir.
 */
public interface CreateOrderUseCase {
    Order createOrder(Order order);
}
