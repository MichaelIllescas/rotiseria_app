package com.imperialnet.foodstore.products.application.ports.in;

import com.imperialnet.foodstore.products.domain.model.Category;

import java.util.Optional;

public interface GetCategoryByIdUseCase {
    Optional<Category> getById(Long id);
}