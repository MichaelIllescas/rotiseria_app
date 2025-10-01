package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.in.GetAllCategoriesUseCase;
import com.imperialnet.foodstore.products.application.ports.out.CategoryRepositoryPort;
import com.imperialnet.foodstore.products.domain.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetAllCategories implements GetAllCategoriesUseCase {

    private final CategoryRepositoryPort categoryRepository;

    public GetAllCategories(CategoryRepositoryPort categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAll() {
        MDC.put("action", "GET_ALL_CATEGORIES");

        try {
            log.info("Obteniendo todas las categorías");
            List<Category> categories = categoryRepository.findAll();
            log.info("Categorías obtenidas correctamente: count={}", categories.size());
            return categories;

        } finally {
            MDC.clear();
        }
    }
}
