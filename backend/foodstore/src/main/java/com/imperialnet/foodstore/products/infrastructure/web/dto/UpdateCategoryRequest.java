package com.imperialnet.foodstore.products.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request para actualizar una categoría.
 */
public record UpdateCategoryRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        String description,

        @NotNull(message = "El estado es obligatorio")
        Boolean active
) {}
