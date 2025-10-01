package com.imperialnet.foodstore.products.domain.service;


import com.imperialnet.foodstore.products.domain.model.Product;

import java.util.List;

/**
 * Servicio de dominio para manejar reglas de stock de productos.
 */
public class StockService {

    /**
     * Descuenta stock de un producto al registrar una venta.
     * @throws IllegalStateException si no hay stock suficiente.
     */
    public void decreaseStock(Product product, int quantity) {
        product.decreaseStock(quantity);
    }

    /**
     * Reinicia el stock diario de un conjunto de productos.
     */
    public void resetDailyStock(List<Product> products, int newStock) {
        products.forEach(p -> p.resetDailyStock(newStock));
    }
}
