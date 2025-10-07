package com.imperialnet.foodstore.products.infrastructure.web.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Request para actualizar un producto existente.
 */
public record UpdateProductRequest(

        @NotNull(message = "La categoría es obligatoria")
        Long categoryId,

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        String description,

        @NotNull(message = "El precio es obligatorio")
        @Min(value = 0, message = "El precio no puede ser negativo")
        BigDecimal price,

        String imageUrl,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer dailyStock

) {}