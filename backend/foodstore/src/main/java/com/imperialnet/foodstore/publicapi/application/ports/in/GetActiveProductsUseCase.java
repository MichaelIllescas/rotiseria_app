package com.imperialnet.foodstore.publicapi.application.ports.in;

import com.imperialnet.foodstore.products.domain.model.Product;
import com.imperialnet.foodstore.products.infrastructure.web.dto.ProductResponse;

import java.util.List;

public interface GetActiveProductsUseCase {
    List<Product> getActiveProducts();
}