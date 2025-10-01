package com.imperialnet.foodstore.products.application.ports.in;

import com.imperialnet.foodstore.products.domain.model.Product;

import java.util.Optional;

public interface GetProductByIdUseCase {
    Optional<Product> getById(Long id);
}
