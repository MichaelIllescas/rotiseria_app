package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.in.GetCategoryByIdUseCase;
import com.imperialnet.foodstore.products.application.ports.out.CategoryRepositoryPort;
import com.imperialnet.foodstore.products.domain.exception.CategoryNotFoundException;
import com.imperialnet.foodstore.products.domain.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class GetCategoryById implements GetCategoryByIdUseCase {

    private final CategoryRepositoryPort categoryRepository;

    public GetCategoryById(CategoryRepositoryPort categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> getById(Long id) {
        MDC.put("action", "GET_CATEGORY_BY_ID");
        MDC.put("categoryId", String.valueOf(id));

        try {
            log.info("Obteniendo categoría con ID: {}", id);

            // Verificar que la categoría existe y obtenerla
            Category category = categoryRepository.findById(id);
            if (category == null) {
                log.warn("Categoría no encontrada con ID: {}", id);
                throw new CategoryNotFoundException("Categoría no encontrada con ID: " + id);
            }

            log.info("Categoría obtenida correctamente: ID={}, nombre='{}'",
                    category.getId(), category.getName());

            return Optional.of(category);

        } catch (CategoryNotFoundException ex) {
            throw ex; // lo captura el ControllerAdvice → 404
        } catch (Exception e) {
            log.error("Error inesperado al obtener categoría con ID: {}. Causa: {}", id, e.getMessage(), e);
            throw e; // lo captura el ControllerAdvice → 500
        } finally {
            MDC.clear();
        }
    }
}
