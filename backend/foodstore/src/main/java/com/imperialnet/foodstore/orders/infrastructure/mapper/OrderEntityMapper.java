package com.imperialnet.foodstore.orders.infrastructure.mapper;

import com.imperialnet.foodstore.orders.domain.model.Order;
import com.imperialnet.foodstore.orders.infrastructure.persistence.entity.OrderEntity;

/**
 * Mapper manual entre Order (dominio) y OrderEntity (JPA).
 * No depende de MapStruct. Implementación a mano.
 */
public interface OrderEntityMapper {

    /**
     * Convierte una entidad de dominio (Order) en una entidad JPA (OrderEntity).
     */
    OrderEntity toEntity(Order order);

    /**
     * Convierte una entidad JPA (OrderEntity) en un modelo de dominio (Order).
     */
    Order toDomain(OrderEntity entity);
}
