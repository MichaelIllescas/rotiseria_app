package com.imperialnet.foodstore.publicapi.application.services;

import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.model.Product;
import com.imperialnet.foodstore.products.infrastructure.mapper.ProductMapper;
import com.imperialnet.foodstore.products.infrastructure.web.dto.ProductResponse;
import com.imperialnet.foodstore.publicapi.application.ports.in.GetActiveProductsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetActiveProductsService implements GetActiveProductsUseCase {

    private final ProductRepositoryPort productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<Product> getActiveProducts() {
        List<Product> activeProducts = productRepository.findActiveProducts();
        return activeProducts;
    }
}
