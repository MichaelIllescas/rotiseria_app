package com.imperialnet.foodstore.products.application.ports.in;

import com.imperialnet.foodstore.products.domain.model.Product;

public interface UpdateProductUseCase {
    Product update(Long id, Product product);
}
