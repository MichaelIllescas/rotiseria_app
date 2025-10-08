package com.imperialnet.foodstore.products.application.ports.in;

/**
 * Caso de uso para actualizar el stock de múltiples productos en una sola operación.
 */
public interface BulkUpdateStockUseCase {
    void bulkUpdateStock(UpdateStockCommand command);
}
