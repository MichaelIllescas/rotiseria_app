package com.imperialnet.foodstore.products.application.ports.in;

import com.imperialnet.foodstore.products.domain.model.Category;

import java.util.List;

public interface GetAllCategoriesUseCase {
    List<Category> getAll();
}