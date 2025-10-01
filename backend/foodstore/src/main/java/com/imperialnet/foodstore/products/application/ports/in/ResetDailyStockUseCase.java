package com.imperialnet.foodstore.products.application.ports.in;

public interface ResetDailyStockUseCase {
    void resetDailyStock(int newStock);
}