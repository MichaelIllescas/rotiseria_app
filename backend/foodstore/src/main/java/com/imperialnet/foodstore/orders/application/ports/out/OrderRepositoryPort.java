package com.imperialnet.foodstore.orders.application.ports.out;

import com.imperialnet.foodstore.orders.domain.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (Output Port)
 * Define las operaciones necesarias para persistir y recuperar pedidos (Order).
 *
 * La capa de infraestructura proveerá una implementación concreta de este puerto.
 */
public interface OrderRepositoryPort {

    /**
     * Guarda un nuevo pedido o actualiza uno existente.
     *
     * @param order Pedido del dominio.
     * @return Pedido persistido (con ID asignado si es nuevo).
     */
    Order save(Order order);

    /**
     * Busca un pedido por su ID.
     *
     * @param id Identificador del pedido.
     * @return Pedido encontrado o vacío si no existe.
     */
    Optional<Order> findById(Long id);

    /**
     * Devuelve todos los pedidos registrados.
     *
     * @return Lista de pedidos.
     */
    List<Order> findAll();

    /**
     * Devuelve los pedidos filtrados por estado.
     *
     * @param status Estado del pedido (RECEIVED, READY, DELIVERED, etc.)
     * @return Lista de pedidos con ese estado.
     */
    List<Order> findByStatus(String status);

    /**
     * Elimina un pedido por su ID (si la política del negocio lo permite).
     * Normalmente, en este dominio se preferiría marcar como cancelado.
     */
    void deleteById(Long id);
}
