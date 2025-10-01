package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.in.ToggleCategoryStatusUseCase;
import com.imperialnet.foodstore.products.application.ports.out.CategoryRepositoryPort;
import com.imperialnet.foodstore.products.domain.exception.CategoryNotFoundException;
import com.imperialnet.foodstore.products.domain.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ToggleCategoryStatus implements ToggleCategoryStatusUseCase {

    private final CategoryRepositoryPort categoryRepository;

    public ToggleCategoryStatus(CategoryRepositoryPort categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void toggleStatus(Long id) {
        MDC.put("action", "TOGGLE_CATEGORY_STATUS");
        MDC.put("categoryId", String.valueOf(id));

        try {
            log.info("Alternando estado de categoría con ID: {}", id);

            // Verificar que la categoría existe y obtener sus datos actuales
            if (!categoryRepository.existsById(id)) {
                log.warn("Intento de cambiar estado de categoría inexistente con ID: {}", id);
                throw new CategoryNotFoundException("Categoría no encontrada con ID: " + id);
            }

            // Obtener la categoría actual
            Category currentCategory = categoryRepository.findById(id);
            boolean currentStatus = currentCategory.isActive();

            // Crear nueva instancia con el estado alternado usando los métodos de dominio
            Category categoryToUpdate = new Category(
                currentCategory.getId(),
                currentCategory.getName(),
                currentCategory.getDescription(),
                currentCategory.isActive()
            );

            // Aplicar la regla de negocio del dominio
            if (currentStatus) {
                categoryToUpdate.deactivate();
            } else {
                categoryToUpdate.activate();
            }

            // Ejecutar actualización
            categoryRepository.update(categoryToUpdate);

            log.info("Estado de categoría con ID: {} cambiado de {} a {}",
                    id, currentStatus, !currentStatus);

        } catch (CategoryNotFoundException ex) {
            throw ex; // lo captura el ControllerAdvice → 404
        } catch (Exception e) {
            log.error("Error inesperado al cambiar estado de categoría con ID: {}. Causa: {}", id, e.getMessage(), e);
            throw e; // lo captura el ControllerAdvice → 500
        } finally {
            MDC.clear();
        }
    }
}
