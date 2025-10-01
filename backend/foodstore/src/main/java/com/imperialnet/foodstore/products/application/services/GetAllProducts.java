package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.in.GetAllProductsUseCase;
import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetAllProducts implements GetAllProductsUseCase {

    private final ProductRepositoryPort productRepository;

    public GetAllProducts(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAll() {
        MDC.put("action", "GET_ALL_PRODUCTS");

        try {
            log.info("Obteniendo todos los productos");
            List<Product> products = productRepository.findAll();
            log.info("Productos obtenidos correctamente: count={}", products.size());
            return products;

        } finally {
            MDC.clear();
        }
    }
}
