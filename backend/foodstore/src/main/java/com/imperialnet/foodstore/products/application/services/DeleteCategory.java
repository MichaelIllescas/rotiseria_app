package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.in.DeleteCategoryUseCase;
import com.imperialnet.foodstore.products.application.ports.out.CategoryRepositoryPort;
import com.imperialnet.foodstore.products.domain.exception.CategoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeleteCategory implements DeleteCategoryUseCase {

    private final CategoryRepositoryPort categoryRepository;

    public DeleteCategory(CategoryRepositoryPort categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void delete(Long id) {
        MDC.put("action", "DELETE_CATEGORY");
        MDC.put("categoryId", String.valueOf(id));

        try {
            log.info("Eliminando categoría con ID: {}", id);

            // Verificar que la categoría existe
            if (!categoryRepository.existsById(id)) {
                log.warn("Intento de eliminar categoría inexistente con ID: {}", id);
                throw new CategoryNotFoundException("Categoría no encontrada con ID: " + id);
            }

            // Ejecutar eliminación
            categoryRepository.deleteById(id);

            log.info("Categoría eliminada correctamente con ID: {}", id);

        } catch (CategoryNotFoundException ex) {
            throw ex; // lo captura el ControllerAdvice → 404
        } catch (Exception e) {
            log.error("Error inesperado al eliminar categoría con ID: {}. Causa: {}", id, e.getMessage(), e);
            throw e; // lo captura el ControllerAdvice → 500
        } finally {
            MDC.clear();
        }
    }
}
