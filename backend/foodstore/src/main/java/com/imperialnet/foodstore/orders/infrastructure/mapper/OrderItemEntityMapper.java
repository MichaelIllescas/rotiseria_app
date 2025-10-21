package com.imperialnet.foodstore.orders.infrastructure.mapper;

import com.imperialnet.foodstore.orders.domain.model.OrderItem;
import com.imperialnet.foodstore.orders.infrastructure.persistence.entity.OrderItemEntity;

/**
 * Mapper manual entre OrderItem (dominio) y OrderItemEntity (JPA).
 * No depende de MapStruct.
 */
public interface OrderItemEntityMapper {

    /**
     * Convierte un modelo de dominio (OrderItem) a una entidad JPA.
     */
    OrderItemEntity toEntity(OrderItem item);

    /**
     * Convierte una entidad JPA (OrderItemEntity) a un modelo de dominio.
     */
    OrderItem toDomain(OrderItemEntity entity);
}
