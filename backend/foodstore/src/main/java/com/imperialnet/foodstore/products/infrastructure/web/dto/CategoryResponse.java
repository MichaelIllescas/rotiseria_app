package com.imperialnet.foodstore.products.infrastructure.web.dto;

/**
 * Respuesta que se envía al cliente al consultar o crear una categoría.
 */
public record CategoryResponse(
        Long id,
        String name,
        String description,
        Boolean active
) {}