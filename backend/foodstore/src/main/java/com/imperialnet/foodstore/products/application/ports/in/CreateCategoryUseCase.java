package com.imperialnet.foodstore.products.application.ports.in;

import com.imperialnet.foodstore.products.domain.model.Category;

public interface CreateCategoryUseCase {
    Category create(Category category);
}
