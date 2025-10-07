package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.in.DeleteProductUseCase;
import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.exception.ProductNotFoundException;
import com.imperialnet.foodstore.products.domain.model.Product;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
public class DeleteProcut implements DeleteProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public DeleteProcut(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public void delete(Long id) {

        Product existing = productRepositoryPort.findById(id);
        if (existing == null) {
            throw new ProductNotFoundException("No existe un producto con el id: " + id);
        }

        MDC.put("action", "DELETE_PRODUCT");
        MDC.put("Product name", existing.getName());

        try {
            productRepositoryPort.deleteById(id);
        } finally {
            MDC.clear();
        }

    }
}
