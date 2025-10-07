package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.exception.ProductNotFoundException;
import com.imperialnet.foodstore.products.domain.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para la clase ToggleProductStatus.
 *
 * Verifica que el servicio alterna correctamente el estado "active" de un producto,
 * maneja el caso de producto inexistente y asegura la limpieza del contexto de logging (MDC)
 * incluso ante errores inesperados. Documentado al estilo de CreateProductTest.
 */
@ExtendWith(MockitoExtension.class)
class ToggleProductStatusTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private ToggleProductStatus toggleProductStatus;

    @AfterEach
    void cleanup() {
        MDC.clear(); // Seguridad adicional si falla una prueba
    }

    /**
     * Test: Alternar de activo(true) a inactivo(false).
     *
     * Escenario:
     * - El producto existe y está activo
     * - Se ejecuta el toggle
     *
     * Comportamiento esperado:
     * - Se consulta existencia por ID y es true
     * - Se trae el producto por ID
     * - Se invoca update con el producto en estado inactivo
     * - El MDC se limpia al finalizar
     */
    @Test
    void deberiaAlternarDeActivoAInactivo() {
        Long id = 1L;
        Product current = new Product(id, 10L, "Pizza", "desc", new BigDecimal("100.00"), "img", 5, true);

        when(productRepository.existsById(id)).thenReturn(true);
        when(productRepository.findById(id)).thenReturn(current);
        when(productRepository.update(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        toggleProductStatus.execute(id);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).update(captor.capture());
        Product updated = captor.getValue();
        assertFalse(updated.isActive(), "El producto debería quedar inactivo");

        verify(productRepository).existsById(id);
        verify(productRepository).findById(id);
        assertNull(MDC.get("action"));
        assertNull(MDC.get("categoryId"));
    }

    /**
     * Test: Alternar de inactivo(false) a activo(true).
     *
     * Escenario:
     * - El producto existe y está inactivo
     * - Se ejecuta el toggle
     *
     * Comportamiento esperado:
     * - Se invoca update con el producto en estado activo
     * - Limpieza de MDC al finalizar
     */
    @Test
    void deberiaAlternarDeInactivoAActivo() {
        Long id = 2L;
        Product current = new Product(id, 11L, "Empanada", "desc", new BigDecimal("50.00"), "img2", 10, false);

        when(productRepository.existsById(id)).thenReturn(true);
        when(productRepository.findById(id)).thenReturn(current);
        when(productRepository.update(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        toggleProductStatus.execute(id);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).update(captor.capture());
        Product updated = captor.getValue();
        assertTrue(updated.isActive(), "El producto debería quedar activo");

        verify(productRepository).existsById(id);
        verify(productRepository).findById(id);
        assertNull(MDC.get("action"));
        assertNull(MDC.get("categoryId"));
    }

    /**
     * Test: Producto inexistente.
     *
     * Escenario:
     * - El repositorio indica que el ID no existe
     *
     * Comportamiento esperado:
     * - Se lanza ProductNotFoundException con el mensaje configurado
     * - NO se invoca findById ni update
     * - El MDC queda limpio
     */
    @Test
    void deberiaLanzarExcepcionSiProductoNoExiste() {
        Long id = 99L;
        when(productRepository.existsById(id)).thenReturn(false);

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class,
                () -> toggleProductStatus.execute(id));
        assertTrue(ex.getMessage().contains("Categoría no encontrada con ID: "));

        verify(productRepository).existsById(id);
        verify(productRepository, never()).findById(anyLong());
        verify(productRepository, never()).update(any());
        assertNull(MDC.get("action"));
        assertNull(MDC.get("categoryId"));
    }

    /**
     * Test: Error inesperado durante la actualización.
     *
     * Escenario:
     * - El producto existe pero al persistir el cambio (update) ocurre un error
     *
     * Comportamiento esperado:
     * - Se propaga una RuntimeException con mensaje genérico y causa original
     * - El MDC se limpia en el bloque finally
     */
    @Test
    void deberiaLimpiarMDCAnteErrorInesperado() {
        Long id = 5L;
        Product current = new Product(id, 12L, "Milanesa", "desc", new BigDecimal("200.00"), "img3", 3, true);
        when(productRepository.existsById(id)).thenReturn(true);
        when(productRepository.findById(id)).thenReturn(current);
        doThrow(new RuntimeException("fallo de BD")).when(productRepository).update(any(Product.class));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> toggleProductStatus.execute(id));
        assertTrue(ex.getMessage().contains("Error inesperado al alternar estado de producto con ID: "));
        assertNotNull(ex.getCause());

        assertNull(MDC.get("action"));
        assertNull(MDC.get("categoryId"));
    }
}

