package com.imperialnet.foodstore.publicapi.application.services;

import com.imperialnet.foodstore.products.application.ports.out.CategoryRepositoryPort;
import com.imperialnet.foodstore.products.domain.model.Category;
import com.imperialnet.foodstore.publicapi.application.ports.in.GetActiveCategoriesUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetActiveCategories implements GetActiveCategoriesUseCase {

    private final CategoryRepositoryPort categoryRepository;

    public GetActiveCategories(CategoryRepositoryPort categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getActiveCategories() {
        return categoryRepository.findActiveCategories();
    }
}
