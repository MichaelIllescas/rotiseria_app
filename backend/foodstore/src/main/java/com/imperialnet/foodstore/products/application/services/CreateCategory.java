package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.in.CreateCategoryUseCase;
import com.imperialnet.foodstore.products.application.ports.out.CategoryRepositoryPort;
import com.imperialnet.foodstore.products.domain.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreateCategory implements CreateCategoryUseCase {

    private final CategoryRepositoryPort categoryRepository;

    public CreateCategory(CategoryRepositoryPort categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category) {
        MDC.put("action", "CREATE_CATEGORY");
        MDC.put("categoryName", category.getName());

        try {
            Category existing = categoryRepository.findByName(category.getName());
            if (existing != null) {
                log.warn("La categoría ya existe");
                throw new IllegalArgumentException(
                        "Ya existe una categoría con el nombre: " + category.getName()
                );
            }

            log.info("Creando categoría: active={}", category.isActive());
            Category saved = categoryRepository.save(category);
            log.info("Categoría creada correctamente: id={}", saved.getId());
            return saved;

        } finally {
            MDC.clear();
        }
    }
}
