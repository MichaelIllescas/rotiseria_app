package com.imperialnet.foodstore.products.application.ports.in;

import com.imperialnet.foodstore.products.domain.model.Product;

import java.util.List;

public interface GetAllProductsUseCase {
    List<Product> getAll();
}