package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.in.UpdateStockUseCase;
import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.exception.ProductNotFoundException;
import com.imperialnet.foodstore.products.domain.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UpdateStockService implements UpdateStockUseCase {

    private final ProductRepositoryPort productRepository;

    public UpdateStockService(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public void updateStock(Long productId, Integer newStock) {
        MDC.put("action", "UPDATE_STOCK");
        if (productId != null) {
            MDC.put("productId", String.valueOf(productId));
        }
        if (newStock != null) {
            MDC.put("newStock", String.valueOf(newStock));
        }

        try {
            log.info("Iniciando actualización de stock");

            Product product = productRepository.findById(productId);
            if (product == null) {
                log.warn("Producto no encontrado");
                throw new ProductNotFoundException("Producto no encontrado con ID: " + productId);
            }

            MDC.put("productName", product.getName());
            if (product.getCategoryId() != null) {
                MDC.put("categoryId", String.valueOf(product.getCategoryId()));
            }

            log.info("Actualizando stock: current={}, new={}", product.getDailyStock(), newStock);
            product.resetDailyStock(newStock);
            productRepository.save(product);
            log.info("Stock actualizado correctamente");

        } finally {
            MDC.clear();
        }
    }
}
