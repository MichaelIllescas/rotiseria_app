package com.imperialnet.foodstore.products.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request para crear una categoría.
 */
public record CreateCategoryRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        String description
) {}
