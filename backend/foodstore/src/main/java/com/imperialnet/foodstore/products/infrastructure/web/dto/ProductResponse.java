package com.imperialnet.foodstore.products.infrastructure.web.dto;

import java.math.BigDecimal;

/**
 * Respuesta que se envía al cliente al consultar o crear un producto.
 */
public record ProductResponse(
        Long id,
        Long categoryId,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        Integer dailyStock,
        Boolean active
) {}
