package com.imperialnet.foodstore.orders.infrastructure.mapper;

import com.imperialnet.foodstore.orders.domain.model.Order;
import com.imperialnet.foodstore.orders.domain.model.OrderItem;
import com.imperialnet.foodstore.orders.infrastructure.persistence.entity.OrderEntity;
import com.imperialnet.foodstore.orders.infrastructure.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación manual del OrderEntityMapper.
 * ✅ Se asegura de mantener la relación bidireccional (Order ↔ Items).
 */
@Component
public class OrderEntityMapperImpl implements OrderEntityMapper {

    private final OrderItemEntityMapper itemMapper;

    public OrderEntityMapperImpl(OrderItemEntityMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public OrderEntity toEntity(Order order) {
        if (order == null) return null;

        OrderEntity entity = new OrderEntity();
        entity.setCustomerName(order.getCustomerName());
        entity.setPhone(order.getPhone());
        entity.setEmail(order.getEmail());
        entity.setDeliveryType(order.getDeliveryType());
        entity.setAddress(order.getAddress());
        entity.setStatus(order.getStatus());
        entity.setPaymentMethod(order.getPaymentMethod());
        entity.setPaymentStatus(order.getPaymentStatus());
        entity.setTotal(order.getTotal());
        entity.setCreatedAt(order.getCreatedAt());

        // 👇 ESTA ES LA CLAVE
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            List<OrderItemEntity> itemEntities = order.getItems().stream()
                    .map(itemMapper::toEntity)
                    .peek(item -> item.setOrder(entity)) // ✅ establece la relación bidireccional
                    .collect(Collectors.toList());

            entity.setItems(itemEntities);
        }

        return entity;
    }

    @Override
    public Order toDomain(OrderEntity entity) {
        if (entity == null) return null;

        Order domain = new Order(
                entity.getId(),
                entity.getCustomerName(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getDeliveryType(),
                entity.getAddress(),
                entity.getPaymentMethod(),
                entity.getStatus(),
                entity.getPaymentStatus(),
                entity.getTotal(),
                null,
                entity.getCreatedAt()
        );

        if (entity.getItems() != null && !entity.getItems().isEmpty()) {
            entity.getItems().stream()
                    .map(itemMapper::toDomain)
                    .forEach(domain::addItem);
        }

        return domain;
    }
}
