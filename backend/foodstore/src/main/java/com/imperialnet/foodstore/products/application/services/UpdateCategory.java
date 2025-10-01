package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.in.UpdateCategoryUseCase;
import com.imperialnet.foodstore.products.application.ports.out.CategoryRepositoryPort;
import com.imperialnet.foodstore.products.domain.exception.CategoryNotFoundException;
import com.imperialnet.foodstore.products.domain.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateCategory implements UpdateCategoryUseCase {

    private final CategoryRepositoryPort categoryRepository;

    public UpdateCategory(CategoryRepositoryPort categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category update(Long id, Category category) {
        MDC.put("action", "UPDATE_CATEGORY");
        MDC.put("categoryId", String.valueOf(id));
        MDC.put("categoryName", category.getName());

        try {
            log.info("Actualizando categoría con ID: {} y nombre: {}", id, category.getName());

            // Verificar que la categoría existe
            if (!categoryRepository.existsById(id)) {
                log.warn("Intento de actualizar categoría inexistente con ID: {}", id);
                throw new CategoryNotFoundException("Categoría no encontrada con ID: " + id);
            }

            // Crear nueva instancia con el ID correcto para la actualización
            Category categoryToUpdate = new Category(id, category.getName(), category.getDescription(), category.isActive());

            // Ejecutar actualización
            Category updatedCategory = categoryRepository.update(categoryToUpdate);

            log.info("Categoría actualizada correctamente con ID: {} y nombre: {}",
                    updatedCategory.getId(), updatedCategory.getName());

            return updatedCategory;

        } catch (CategoryNotFoundException ex) {
            throw ex; // lo captura el ControllerAdvice → 404
        } catch (Exception e) {
            log.error("Error inesperado al actualizar categoría con ID: {} y nombre: {}. Causa: {}",
                    id, category.getName(), e.getMessage(), e);
            throw e; // lo captura el ControllerAdvice → 500
        } finally {
            MDC.clear();
        }
    }
}
