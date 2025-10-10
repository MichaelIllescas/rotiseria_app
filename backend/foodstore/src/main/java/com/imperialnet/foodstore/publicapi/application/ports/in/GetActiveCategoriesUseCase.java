package com.imperialnet.foodstore.publicapi.application.ports.in;

import com.imperialnet.foodstore.products.domain.model.Category;

import java.util.List;

public interface GetActiveCategoriesUseCase {
    List<Category> getActiveCategories();
}
