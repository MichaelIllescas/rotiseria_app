package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.exception.ProductNotFoundException;
import com.imperialnet.foodstore.products.domain.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para la clase DeleteProcut.
 *
 * Verifica que el servicio de eliminación de productos funcione correctamente en diferentes escenarios:
 * - Eliminación exitosa
 * - Producto inexistente
 * - Excepción durante la eliminación, asegurando limpieza del MDC
 *
 * La documentación replica el estilo de CreateProductTest para mantener consistencia.
 */
@ExtendWith(MockitoExtension.class)
class DeleteProcutTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private DeleteProcut deleteProcut;

    @AfterEach
    void cleanup() {
        MDC.clear(); // Seguridad adicional si falla la prueba
    }

    /**
     * Test: Verificar eliminación exitosa de un producto existente.
     *
     * Escenario:
     * - Existe un producto con el ID indicado
     * - Se ejecuta la eliminación sin errores
     *
     * Comportamiento esperado:
     * - Se consulta el producto por ID y se encuentra
     * - Se invoca deleteById en el repositorio
     * - El MDC se limpia correctamente tras la operación
     *
     * Verificaciones:
     * - Se llama a findById con el ID
     * - Se llama a deleteById con el ID
     * - action y "Product name" no quedan en MDC
     */
    @Test
    void deberiaEliminarProductoExistente() {
        Long id = 7L;
        Product existente = new Product(id, 1L, "Lomito Completo", "Con papas",
                new BigDecimal("2500.00"), "lomito.jpg", 10, true);

        when(productRepositoryPort.findById(id)).thenReturn(existente);
        doNothing().when(productRepositoryPort).deleteById(id);

        deleteProcut.delete(id);

        verify(productRepositoryPort).findById(id);
        verify(productRepositoryPort).deleteById(id);
        assertNull(MDC.get("action"));
        assertNull(MDC.get("Product name"));
    }

    /**
     * Test: Verificar que se lance excepción si el producto no existe.
     *
     * Escenario:
     * - No existe un producto con el ID indicado
     *
     * Comportamiento esperado:
     * - Se lanza ProductNotFoundException con mensaje descriptivo
     * - NO se invoca deleteById
     * - El MDC se mantiene limpio
     *
     * Verificaciones:
     * - Se llama a findById
     * - No se llama a deleteById
     * - action y "Product name" no quedan en MDC
     */
    @Test
    void deberiaLanzarExcepcionSiProductoNoExiste() {
        Long id = 99L;
        when(productRepositoryPort.findById(id)).thenReturn(null);

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> deleteProcut.delete(id));
        assertTrue(ex.getMessage().contains("No existe un producto con el id: "));

        verify(productRepositoryPort).findById(id);
        verify(productRepositoryPort, never()).deleteById(anyLong());
        assertNull(MDC.get("action"));
        assertNull(MDC.get("Product name"));
    }

    /**
     * Test: Verificar limpieza del MDC cuando ocurre una excepción durante deleteById.
     *
     * Escenario:
     * - El producto existe
     * - deleteById lanza una RuntimeException simulando un error de persistencia
     *
     * Comportamiento esperado:
     * - La excepción se propaga
     * - Aún así, el bloque finally limpia el MDC correctamente
     *
     * Verificaciones:
     * - action y "Product name" no quedan en MDC tras la excepción
     */
    @Test
    void deberiaLimpiarMDCInclusoSiFallaDelete() {
        Long id = 5L;
        Product existente = new Product(id, 2L, "Empanada de Pollo", "Horneada",
                new BigDecimal("500.00"), "empanada.jpg", 30, true);

        when(productRepositoryPort.findById(id)).thenReturn(existente);
        doThrow(new RuntimeException("DB down")).when(productRepositoryPort).deleteById(id);

        assertThrows(RuntimeException.class, () -> deleteProcut.delete(id));

        assertNull(MDC.get("action"));
        assertNull(MDC.get("Product name"));
    }
}

