package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
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
 * Suite de tests unitarios para la clase CreateProduct.
 *
 * Verifica que el servicio de creación de productos funcione correctamente
 * en diferentes escenarios: creación exitosa, productos duplicados,
 * y limpieza adecuada del contexto de logging (MDC).
 */
@ExtendWith(MockitoExtension.class)
class CreateProductTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private CreateProduct createProduct;

    @AfterEach
    void cleanup() {
        MDC.clear(); // Seguridad adicional si falla la prueba
    }

    /**
     * Test: Verificar creación exitosa de un nuevo producto.
     *
     * Escenario: Se intenta crear un producto con un nombre que no existe en el sistema.
     *
     * Comportamiento esperado:
     * - El repositorio no encuentra un producto existente (retorna null)
     * - Se guarda el nuevo producto en el repositorio
     * - Se retorna el producto con el ID asignado por persistencia
     * - Se limpia correctamente el contexto MDC de logging
     *
     * Verificaciones:
     * - El producto retornado no es nulo
     * - El ID asignado es correcto (10L)
     * - El nombre se mantiene ("Pizza Muzzarella")
     * - El precio y otros atributos se conservan
     * - Se llama a findByName y save en el repositorio
     * - El MDC queda limpio después de la operación
     */
    @Test
    void deberiaCrearProductoCuandoNoExiste() {
        Product nuevo = new Product(null, 1L, "Pizza Muzzarella", "Clásica con muzza",
                BigDecimal.valueOf(1500), "pizza.jpg", 10, true);
        Product persistido = new Product(10L, 1L, "Pizza Muzzarella", "Clásica con muzza",
                BigDecimal.valueOf(1500), "pizza.jpg", 10, true);

        when(productRepository.findByName("Pizza Muzzarella")).thenReturn(null);
        when(productRepository.save(nuevo)).thenReturn(persistido);

        Product result = createProduct.create(nuevo);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Pizza Muzzarella", result.getName());
        assertEquals(BigDecimal.valueOf(1500), result.getPrice());
        assertEquals(1L, result.getCategoryId());
        verify(productRepository).findByName("Pizza Muzzarella");
        verify(productRepository).save(nuevo);
        assertNull(MDC.get("action"));
        assertNull(MDC.get("productName"));
        assertNull(MDC.get("categoryId"));
    }

    /**
     * Test: Verificar que se lance excepción al intentar crear producto duplicado.
     *
     * Escenario: Se intenta crear un producto con un nombre que ya existe en el sistema.
     *
     * Comportamiento esperado:
     * - El repositorio encuentra un producto existente con el mismo nombre
     * - Se lanza IllegalArgumentException con mensaje descriptivo
     * - NO se intenta guardar el producto duplicado
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se lanza la excepción esperada (IllegalArgumentException)
     * - El mensaje de excepción contiene el nombre del producto
     * - Se llama a findByName pero NO a save
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void deberiaLanzarExcepcionSiProductoExiste() {
        Product existente = new Product(5L, 2L, "Empanada de Carne", "Empanada casera",
                BigDecimal.valueOf(300), "empanada.jpg", 20, true);
        when(productRepository.findByName("Empanada de Carne")).thenReturn(existente);

        Product intento = new Product(null, 2L, "Empanada de Carne", "Nueva empanada",
                BigDecimal.valueOf(350), "empanada2.jpg", 15, true);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> createProduct.create(intento));

        assertTrue(ex.getMessage().contains("Empanada de Carne"));
        verify(productRepository).findByName("Empanada de Carne");
        verify(productRepository, never()).save(any());
        assertNull(MDC.get("action"));
        assertNull(MDC.get("productName"));
        assertNull(MDC.get("categoryId"));
    }

    /**
     * Test: Verificar limpieza de MDC cuando ocurre una excepción.
     *
     * Escenario: Se produce una excepción durante el proceso de creación de producto.
     *
     * Comportamiento esperado:
     * - Independientemente de que se lance una excepción, el bloque finally
     *   debe ejecutarse y limpiar el contexto MDC
     * - No deben quedar rastros de información de logging en el contexto global
     *
     * Verificaciones:
     * - Después de la excepción, action, productName y categoryId no están en MDC
     * - Se confirma que el try-finally funciona correctamente
     */
    @Test
    void deberiaLimpiarMDCTrasExcepcion() {
        Product existente = new Product(3L, 1L, "Milanesa Napolitana", "Con jamón y queso",
                BigDecimal.valueOf(2000), "milanesa.jpg", 5, true);
        when(productRepository.findByName("Milanesa Napolitana")).thenReturn(existente);

        Product intento = new Product(null, 1L, "Milanesa Napolitana", "Diferente descripción",
                BigDecimal.valueOf(2100), "milanesa2.jpg", 8, true);

        assertThrows(IllegalArgumentException.class, () -> createProduct.create(intento));

        assertNull(MDC.get("action"));
        assertNull(MDC.get("productName"));
        assertNull(MDC.get("categoryId"));
    }

    /**
     * Test: Verificar limpieza de MDC tras operación exitosa.
     *
     * Escenario: Se completa exitosamente la creación de un producto.
     *
     * Comportamiento esperado:
     * - Después de una operación exitosa, el contexto MDC debe quedar limpio
     * - No deben persistir datos de logging entre diferentes operaciones
     *
     * Verificaciones:
     * - Tras completar la creación, action, productName y categoryId no están en MDC
     * - Se confirma que la limpieza ocurre en operaciones exitosas
     *
     * Nota: Este test es crucial para evitar contaminación de logs entre
     * diferentes operaciones en un entorno multi-hilo.
     */
    @Test
    void deberiaLimpiarMDCTrasOperacionExitosa() {
        Product nuevo = new Product(null, 3L, "Tarta de Jamón y Queso", "Casera",
                BigDecimal.valueOf(800), "tarta.jpg", 12, true);
        Product persistido = new Product(22L, 3L, "Tarta de Jamón y Queso", "Casera",
                BigDecimal.valueOf(800), "tarta.jpg", 12, true);

        when(productRepository.findByName("Tarta de Jamón y Queso")).thenReturn(null);
        when(productRepository.save(nuevo)).thenReturn(persistido);

        createProduct.create(nuevo);

        assertNull(MDC.get("action"));
        assertNull(MDC.get("productName"));
        assertNull(MDC.get("categoryId"));
    }

    /**
     * Test: Verificar creación de producto sin categoría asignada.
     *
     * Escenario: Se crea un producto que no tiene categoryId (null).
     *
     * Comportamiento esperado:
     * - La creación debe proceder normalmente
     * - El MDC no debe incluir categoryId cuando es null
     * - El producto se guarda correctamente sin categoría
     *
     * Verificaciones:
     * - El producto se crea exitosamente
     * - No se produce error por categoryId null
     * - El MDC se limpia correctamente
     */
    @Test
    void deberiaCrearProductoSinCategoria() {
        Product nuevo = new Product(null, null, "Producto Especial", "Sin categoría",
                BigDecimal.valueOf(500), "especial.jpg", 5, true);
        Product persistido = new Product(15L, null, "Producto Especial", "Sin categoría",
                BigDecimal.valueOf(500), "especial.jpg", 5, true);

        when(productRepository.findByName("Producto Especial")).thenReturn(null);
        when(productRepository.save(nuevo)).thenReturn(persistido);

        Product result = createProduct.create(nuevo);

        assertNotNull(result);
        assertEquals(15L, result.getId());
        assertNull(result.getCategoryId());
        verify(productRepository).findByName("Producto Especial");
        verify(productRepository).save(nuevo);
        assertNull(MDC.get("action"));
        assertNull(MDC.get("productName"));
        assertNull(MDC.get("categoryId"));
    }
}
