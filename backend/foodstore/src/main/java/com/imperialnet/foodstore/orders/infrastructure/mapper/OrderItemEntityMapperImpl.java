package com.imperialnet.foodstore.orders.infrastructure.mapper;

import com.imperialnet.foodstore.orders.domain.model.OrderItem;
import com.imperialnet.foodstore.orders.infrastructure.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

/**
 * Implementación manual del mapper entre OrderItem y OrderItemEntity.
 */
@Component
public class OrderItemEntityMapperImpl implements OrderItemEntityMapper {

    @Override
    public OrderItemEntity toEntity(OrderItem item) {
        if (item == null) return null;

        OrderItemEntity entity = new OrderItemEntity();
        entity.setProductId(item.getProductId());
        entity.setProductName(item.getProductName());
        entity.setUnitPrice(item.getUnitPrice());
        entity.setQuantity(item.getQuantity());
        return entity;
    }

    @Override
    public OrderItem toDomain(OrderItemEntity entity) {
        if (entity == null) return null;

        // Se crea el ítem solo con ID y cantidad (constructor mínimo)
        OrderItem orderItem = new OrderItem(entity.getProductId(), entity.getQuantity());

        // Luego se completan los datos del producto (nombre y precio)
        orderItem.completeProductData(entity.getProductName(), entity.getUnitPrice());
        return orderItem;
    }
}
