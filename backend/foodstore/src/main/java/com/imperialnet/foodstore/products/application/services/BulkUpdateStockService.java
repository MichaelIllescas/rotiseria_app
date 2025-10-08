package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.in.BulkUpdateStockUseCase;
import com.imperialnet.foodstore.products.application.ports.in.UpdateStockCommand;
import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de aplicación para actualizar múltiples productos en una sola operación.
 */
@Service
public class BulkUpdateStockService implements BulkUpdateStockUseCase {

    private final ProductRepositoryPort productRepository;

    public BulkUpdateStockService(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void bulkUpdateStock(UpdateStockCommand command) {
        List<Product> productsToUpdate = new ArrayList<>();

        for (var item : command.items()) {
            Product product = productRepository.findById(item.productId());

            if (product == null) {
                throw new IllegalArgumentException("Producto no encontrado: " + item.productId());
            }

            product.resetDailyStock(item.stock());
            productsToUpdate.add(product);
        }

        productRepository.saveAll(productsToUpdate);
    }
}
