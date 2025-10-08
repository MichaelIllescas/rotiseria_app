package com.imperialnet.foodstore.products.infrastructure.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para actualizar el stock de un producto.
 */
public record UpdateStockRequest(

        @NotNull(message = "El ID del producto es obligatorio.")
        Long productId,

        @NotNull(message = "El stock no puede ser nulo.")
        @Min(value = 0, message = "El stock no puede ser negativo.")
            Integer stock

) {}
