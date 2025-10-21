package com.imperialnet.foodstore.orders.infrastructure.web.dto;


import java.math.BigDecimal;

/**
 * DTO para devolver ítems de pedido (Response).
 */
public record OrderItemResponse(
        Long productId,
        String productName,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subtotal
) {}
