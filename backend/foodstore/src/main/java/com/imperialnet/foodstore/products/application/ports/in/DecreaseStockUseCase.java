package com.imperialnet.foodstore.products.application.ports.in;

public interface DecreaseStockUseCase {
    void decreaseStock(Long productId, int quantity);
}