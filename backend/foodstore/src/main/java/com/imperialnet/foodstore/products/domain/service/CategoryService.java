package com.imperialnet.foodstore.products.domain.service;

import com.imperialnet.foodstore.products.domain.model.Category;

/**
 * Servicio de dominio para manejar reglas de categorías.
 */
public class CategoryService {

    /**
     * Cambia el estado de una categoría.
     */
    public void toggleActive(Category category) {
        if (category.isActive()) {
            category.deactivate();
        } else {
            category.activate();
        }
    }
}
