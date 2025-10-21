package com.imperialnet.foodstore.orders.infrastructure.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada para un ítem del pedido.
 * Solo contiene datos mínimos: id del producto y cantidad.
 */
public record OrderItemRequest(

        @NotNull(message = "El ID del producto es obligatorio.")
        Long productId,

        @NotNull(message = "La cantidad es obligatoria.")
        @Min(value = 1, message = "La cantidad debe ser mayor que cero.")
        Integer quantity
) {}
