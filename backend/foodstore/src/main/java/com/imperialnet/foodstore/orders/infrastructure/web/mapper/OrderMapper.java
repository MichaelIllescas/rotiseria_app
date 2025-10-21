package com.imperialnet.foodstore.orders.infrastructure.web.mapper;

import com.imperialnet.foodstore.orders.domain.model.Order;
import com.imperialnet.foodstore.orders.domain.model.OrderItem;

import com.imperialnet.foodstore.orders.infrastructure.web.dto.OrderItemRequest;
import com.imperialnet.foodstore.orders.infrastructure.web.dto.OrderItemResponse;
import com.imperialnet.foodstore.orders.infrastructure.web.dto.OrderRequest;
import com.imperialnet.foodstore.orders.infrastructure.web.dto.OrderResponse;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper MapStruct para convertir entre:
 * - OrderRequest ↔ Order (entrada)
 * - Order ↔ OrderResponse (salida)
 */
@Mapper(componentModel = "spring", uses = OrderItemDtoMapper.class)
public interface OrderMapper {

    // ============================
    // 🔹 Request → Domain
    // ============================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "total", ignore = true)
    Order toDomain(OrderRequest request);

    // ============================
    // 🔹 Domain → Response
    // ============================
    @Mapping(target = "items", source = "items")
    OrderResponse toResponse(Order order);

    // ============================
    // 🔹 Listas
    // ============================
    List<OrderResponse> toResponseList(List<Order> orders);

    // ============================
    // 🔹 Métodos auxiliares (opcional)
    // ============================
    @Named("toDomainItem")
    default OrderItem toDomainItem(OrderItemRequest dto, @Context OrderItemDtoMapper mapper) {
        return mapper.toDomain(dto);
    }

    @Named("toResponseItem")
    default OrderItemResponse toResponseItem(OrderItem item, @Context OrderItemDtoMapper mapper) {
        return mapper.toResponse(item);
    }
}
