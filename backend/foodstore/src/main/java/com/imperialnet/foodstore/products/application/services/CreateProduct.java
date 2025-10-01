package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.in.CreateProductUseCase;
import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreateProduct implements CreateProductUseCase {

    private final ProductRepositoryPort productRepository;

    public CreateProduct(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(Product product) {
        MDC.put("action", "CREATE_PRODUCT");
        MDC.put("productName", product.getName());
        if (product.getCategoryId() != null) {
            MDC.put("categoryId", product.getCategoryId().toString());
        }

        try {
            Product existing = productRepository.findByName(product.getName());
            if (existing != null) {
                log.warn("El producto ya existe");
                throw new IllegalArgumentException(
                        "Ya existe un producto con el nombre: " + product.getName()
                );
            }

            log.info("Creando producto: price={}, active={}", product.getPrice(), product.isActive());
            Product saved = productRepository.save(product);
            log.info("Producto creado correctamente: id={}", saved.getId());
            return saved;

        } finally {
            MDC.clear();
        }
    }
}
