package com.imperialnet.foodstore.orders.infrastructure.web.mapper;


import com.imperialnet.foodstore.orders.domain.model.OrderItem;

import com.imperialnet.foodstore.orders.infrastructure.web.dto.OrderItemRequest;
import com.imperialnet.foodstore.orders.infrastructure.web.dto.OrderItemResponse;
import org.mapstruct.*;

/**
 * Mapper para convertir entre OrderItem y sus DTOs.
 */
@Mapper(componentModel = "spring")
public interface OrderItemDtoMapper {

    // ============================
    // 🔹 Request → Domain
    // ============================
    @Mapping(target = "productName", ignore = true)
    @Mapping(target = "unitPrice", ignore = true)
    OrderItem toDomain(OrderItemRequest dto);

    // ============================
    // 🔹 Domain → Response
    // ============================
    @Mapping(target = "subtotal", expression = "java(item.getSubtotal())")
    OrderItemResponse toResponse(OrderItem item);
}
