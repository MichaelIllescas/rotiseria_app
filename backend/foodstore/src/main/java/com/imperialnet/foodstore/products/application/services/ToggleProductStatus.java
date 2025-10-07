package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.in.ToggleProducStatusUseCase;
import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.exception.ProductNotFoundException;
import com.imperialnet.foodstore.products.domain.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ToggleProductStatus implements ToggleProducStatusUseCase {

    private final ProductRepositoryPort productRepository;

    public ToggleProductStatus(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void execute(Long id) {
        MDC.put("action", "PRODUCT_TOGGLE_STATUS");
        MDC.put("categoryId", String.valueOf(id));

        try {
           log.info("Alternando estado de categoría con ID: {}", id);

           if(!productRepository.existsById(id)) {
                log.warn("Intento de cambiar estado de producto inexistente con ID: {}", id);
                throw new ProductNotFoundException("Categoría no encontrada con ID: " + id);
            }

            Product currentProduct = productRepository.findById(id);
            boolean currentStatus = currentProduct.isActive();
            if (currentStatus) {
                currentProduct.deactivate();
            } else {
                currentProduct.activate();
            }
            productRepository.update(currentProduct);
            log.info("Estado de producto con ID: {} cambiado de {} a {}",
                    id, currentStatus, !currentStatus);

        } catch (ProductNotFoundException ex) {
            log.error("Error al alternar estado de producto con ID: {}: {}", id, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error inesperado al alternar estado de producto con ID: {}: {}", id, ex.getMessage());
            throw new RuntimeException("Error inesperado al alternar estado de producto con ID: " + id, ex);
        } finally {
            MDC.clear();
        }

    }
}
