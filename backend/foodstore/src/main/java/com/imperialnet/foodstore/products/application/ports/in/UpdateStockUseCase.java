package com.imperialnet.foodstore.products.application.ports.in;

public interface UpdateStockUseCase {
    void updateStock(Long productId, Integer newStock);
}