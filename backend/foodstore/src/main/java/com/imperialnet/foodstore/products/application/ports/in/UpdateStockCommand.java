package com.imperialnet.foodstore.products.application.ports.in;

import java.util.List;

/**
 * Comando de entrada para actualizar el stock de múltiples productos.
 */
public record UpdateStockCommand(List<ProductStockItem> items) {

    /**
     * Representa el nuevo stock de un producto específico.
     */
    public record ProductStockItem(Long productId, Integer stock) {}
}
