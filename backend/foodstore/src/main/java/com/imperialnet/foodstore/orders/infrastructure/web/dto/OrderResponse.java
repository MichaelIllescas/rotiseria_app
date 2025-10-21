package com.imperialnet.foodstore.orders.infrastructure.web.dto;

import com.imperialnet.foodstore.orders.domain.model.*;
import com.imperialnet.foodstore.orders.infrastructure.web.dto.OrderItemResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para devolver la información completa de un pedido.
 */
public record OrderResponse(
        Long id,
        LocalDateTime createdAt,
        String customerName,
        String phone,
        DeliveryType deliveryType,
        String address,
        OrderStatus status,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        BigDecimal total,
        List<OrderItemResponse> items
) {}
