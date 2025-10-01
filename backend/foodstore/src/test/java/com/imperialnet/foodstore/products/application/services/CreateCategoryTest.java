package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.out.CategoryRepositoryPort;
import com.imperialnet.foodstore.products.domain.model.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para la clase CreateCategory.
 *
 * Verifica que el servicio de creación de categorías funcione correctamente
 * en diferentes escenarios: creación exitosa, categorías duplicadas,
 * y limpieza adecuada del contexto de logging (MDC).
 */
@ExtendWith(MockitoExtension.class)
class CreateCategoryTest {

    @Mock
    private CategoryRepositoryPort categoryRepository;

    @InjectMocks
    private CreateCategory createCategory;

    @AfterEach
    void cleanup() {
        MDC.clear(); // Seguridad adicional si falla la prueba
    }

    /**
     * Test: Verificar creación exitosa de una nueva categoría.
     *
     * Escenario: Se intenta crear una categoría con un nombre que no existe en el sistema.
     *
     * Comportamiento esperado:
     * - El repositorio no encuentra una categoría existente (retorna null)
     * - Se guarda la nueva categoría en el repositorio
     * - Se retorna la categoría con el ID asignado por persistencia
     * - Se limpia correctamente el contexto MDC de logging
     *
     * Verificaciones:
     * - La categoría retornada no es nula
     * - El ID asignado es correcto (10L)
     * - El nombre se mantiene ("Bebidas")
     * - Se llama a findByName y save en el repositorio
     * - El MDC queda limpio después de la operación
     */
    @Test
    void deberiaCrearCategoriaCuandoNoExiste() {
        Category nueva = new Category(null, "Bebidas", "Frías y calientes", true);
        Category persistida = new Category(10L, "Bebidas", "Frías y calientes", true);

        when(categoryRepository.findByName("Bebidas")).thenReturn(null);
        when(categoryRepository.save(nueva)).thenReturn(persistida);

        Category result = createCategory.create(nueva);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Bebidas", result.getName());
        verify(categoryRepository).findByName("Bebidas");
        verify(categoryRepository).save(nueva);
        assertNull(MDC.get("action"));
        assertNull(MDC.get("categoryName"));
    }

    /**
     * Test: Verificar que se lance excepción al intentar crear categoría duplicada.
     *
     * Escenario: Se intenta crear una categoría con un nombre que ya existe en el sistema.
     *
     * Comportamiento esperado:
     * - El repositorio encuentra una categoría existente con el mismo nombre
     * - Se lanza IllegalArgumentException con mensaje descriptivo
     * - NO se intenta guardar la categoría duplicada
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se lanza la excepción esperada (IllegalArgumentException)
     * - El mensaje de excepción contiene el nombre de la categoría
     * - Se llama a findByName pero NO a save
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void deberiaLanzarExcepcionSiCategoriaExiste() {
        Category existente = new Category(5L, "Snacks", "Salados", true);
        when(categoryRepository.findByName("Snacks")).thenReturn(existente);

        Category intento = new Category(null, "Snacks", "Salados", true);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> createCategory.create(intento));

        assertTrue(ex.getMessage().contains("Snacks"));
        verify(categoryRepository).findByName("Snacks");
        verify(categoryRepository, never()).save(any());
        assertNull(MDC.get("action"));
        assertNull(MDC.get("categoryName"));
    }

    /**
     * Test: Verificar limpieza de MDC cuando ocurre una excepción.
     *
     * Escenario: Se produce una excepción durante el proceso de creación de categoría.
     *
     * Comportamiento esperado:
     * - Independientemente de que se lance una excepción, el bloque finally
     *   debe ejecutarse y limpiar el contexto MDC
     * - No deben quedar rastros de información de logging en el contexto global
     *
     * Verificaciones:
     * - Después de la excepción, action y categoryName no están en MDC
     * - Se confirma que el try-finally funciona correctamente
     */
    @Test
    void deberiaLimpiarMDCTrasExcepcion() {
        Category existente = new Category(3L, "Postres", null, true);
        when(categoryRepository.findByName("Postres")).thenReturn(existente);

        Category intento = new Category(null, "Postres", null, true);

        assertThrows(IllegalArgumentException.class, () -> createCategory.create(intento));

        assertNull(MDC.get("action"));
        assertNull(MDC.get("categoryName"));
    }

    /**
     * Test: Verificar limpieza de MDC tras operación exitosa.
     *
     * Escenario: Se completa exitosamente la creación de una categoría.
     *
     * Comportamiento esperado:
     * - Después de una operación exitosa, el contexto MDC debe quedar limpio
     * - No deben persistir datos de logging entre diferentes operaciones
     *
     * Verificaciones:
     * - Tras completar la creación, action y categoryName no están en MDC
     * - Se confirma que la limpieza ocurre en operaciones exitosas
     *
     * Nota: Este test es crucial para evitar contaminación de logs entre
     * diferentes operaciones en un entorno multi-hilo.
     */
    @Test
    void deberiaLimpiarMDCTrasOperacionExitosa() {
        Category nueva = new Category(null, "Lacteos", null, true);
        Category persistida = new Category(22L, "Lacteos", null, true);

        when(categoryRepository.findByName("Lacteos")).thenReturn(null);
        when(categoryRepository.save(nueva)).thenReturn(persistida);

        createCategory.create(nueva);

        assertNull(MDC.get("action"));
        assertNull(MDC.get("categoryName"));
    }
}
