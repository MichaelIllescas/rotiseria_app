package com.imperialnet.foodstore.products.domain.service;


import com.imperialnet.foodstore.products.domain.model.Category;
import com.imperialnet.foodstore.products.domain.model.Product;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de dominio para operaciones del catálogo.
 */
public class CatalogService {

    /**
     * Devuelve los productos activos de una categoría.
     */
    public List<Product> getActiveProductsByCategory(Category category, List<Product> allProducts) {
        return allProducts.stream()
                .filter(Product::isActive)
                .filter(p -> p.getCategoryId().equals(category.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Verifica si se puede eliminar una categoría sin productos asociados.
     */
    public boolean canDeleteCategory(Category category, List<Product> allProducts) {
        return allProducts.stream()
                .noneMatch(p -> p.getCategoryId().equals(category.getId()));
    }
}
