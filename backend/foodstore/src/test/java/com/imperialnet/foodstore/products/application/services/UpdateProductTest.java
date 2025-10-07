package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.out.CategoryRepositoryPort;
import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.exception.CategoryNotFoundException;
import com.imperialnet.foodstore.products.domain.exception.DuplicateProductNameException;
import com.imperialnet.foodstore.products.domain.exception.ProductNotFoundException;
import com.imperialnet.foodstore.products.domain.model.Category;
import com.imperialnet.foodstore.products.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para la clase UpdateProduct.
 *
 * Verifica que el servicio de actualización de productos funcione correctamente
 * en distintos escenarios: actualización exitosa, producto inexistente,
 * conflicto por nombre duplicado, categoría inexistente y actualización sin categoría.
 *
 * El estilo de documentación replica el de CreateProductTest para mantener
 * consistencia y claridad en los objetivos de cada prueba.
 */
@ExtendWith(MockitoExtension.class)
class UpdateProductTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;

    private UpdateProduct updateProduct;

    @BeforeEach
    void setUp() {
        updateProduct = new UpdateProduct(productRepositoryPort, categoryRepositoryPort);
    }

    /**
     * Test: Actualización exitosa cuando el nombre no cambia.
     *
     * Escenario: Existe un producto y se actualizan sus campos (desc, precio, imagen, stock, activo)
     * manteniendo el mismo nombre y una categoría válida.
     *
     * Comportamiento esperado:
     * - Se busca el producto por ID y se encuentra
     * - NO se verifica duplicidad de nombre porque no cambió
     * - Se valida la categoría indicada
     * - Se realiza el update y se devuelve el producto actualizado
     *
     * Verificaciones:
     * - Se invoca findById y update en el repositorio
     * - NO se invoca findByName
     * - Se invoca findById de categoría con el ID correcto
     * - Los campos actualizados coinciden con el input
     */
    @Test
    void should_update_product_when_valid_input_and_name_unchanged() {
        Long id = 1L;
        Long categoryId = 10L;
        Product existing = new Product(id, categoryId, "Pizza", "desc", new BigDecimal("100.00"), "img1", 5, true);
        when(productRepositoryPort.findById(id)).thenReturn(existing);
        when(categoryRepositoryPort.findById(categoryId)).thenReturn(new Category(categoryId, "Pizzas", null, true));
        // echo-update behaviour
        when(productRepositoryPort.update(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product input = new Product(null, categoryId, "Pizza", "nueva desc", new BigDecimal("120.00"), "img2", 7, false);

        Product result = updateProduct.update(id, input);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(categoryId, result.getCategoryId());
        assertEquals("Pizza", result.getName());
        assertEquals("nueva desc", result.getDescription());
        assertEquals(new BigDecimal("120.00"), result.getPrice());
        assertEquals("img2", result.getImageUrl());
        assertEquals(7, result.getDailyStock());
        assertFalse(result.isActive());

        // verify interactions
        verify(productRepositoryPort, times(1)).findById(id);
        verify(productRepositoryPort, never()).findByName(anyString());
        verify(categoryRepositoryPort, times(1)).findById(categoryId);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepositoryPort, times(1)).update(captor.capture());
        Product sent = captor.getValue();
        assertEquals(id, sent.getId());
        assertEquals(categoryId, sent.getCategoryId());
        assertEquals("Pizza", sent.getName());
    }

    /**
     * Test: Falla si el producto no existe.
     *
     * Escenario: Se intenta actualizar un ID que no corresponde a ningún producto.
     *
     * Comportamiento esperado:
     * - La búsqueda por ID devuelve null
     * - Se lanza ProductNotFoundException
     * - No se intenta actualizar ni validar categoría
     *
     * Verificaciones:
     * - Se invoca findById una sola vez
     * - NO se invoca update ni búsquedas de categoría
     */
    @Test
    void should_throw_when_product_not_found() {
        Long id = 99L;
        when(productRepositoryPort.findById(id)).thenReturn(null);

        Product input = new Product(null, null, "Nuevo", new String("x"), new BigDecimal("10.00"), null, 0, true);

        assertThrows(ProductNotFoundException.class, () -> updateProduct.update(id, input));

        verify(productRepositoryPort, times(1)).findById(id);
        verify(productRepositoryPort, never()).update(any());
        verify(categoryRepositoryPort, never()).findById(any());
    }

    /**
     * Test: Falla al renombrar a un nombre ya existente.
     *
     * Escenario: El producto existe pero se intenta cambiar su nombre por uno
     * que ya está siendo usado por otro producto distinto.
     *
     * Comportamiento esperado:
     * - Se encuentra el producto por ID
     * - Se consulta por nombre y existe un conflicto con otro ID
     * - Se lanza DuplicateProductNameException
     * - No se hace update ni validación de categoría
     *
     * Verificaciones:
     * - Se invoca findById y findByName con el nuevo nombre
     * - NO se invoca update
     */
    @Test
    void should_throw_when_renaming_to_duplicate_name() {
        Long id = 2L;
        Product existing = new Product(id, null, "Pizza", "d", new BigDecimal("50.00"), null, 1, true);
        when(productRepositoryPort.findById(id)).thenReturn(existing);

        Product conflict = new Product(77L, null, "Empanada", "d2", new BigDecimal("30.00"), null, 1, true);
        when(productRepositoryPort.findByName("Empanada")).thenReturn(conflict);

        Product input = new Product(null, null, "Empanada", "otra", new BigDecimal("60.00"), "img", 2, true);

        assertThrows(DuplicateProductNameException.class, () -> updateProduct.update(id, input));

        verify(productRepositoryPort, times(1)).findById(id);
        verify(productRepositoryPort, times(1)).findByName("Empanada");
        verify(productRepositoryPort, never()).update(any());
        verify(categoryRepositoryPort, never()).findById(any());
    }

    /**
     * Test: Falla si la categoría indicada no existe.
     *
     * Escenario: El producto existe, se actualiza con un categoryId que no está registrado.
     *
     * Comportamiento esperado:
     * - Se encuentra el producto por ID
     * - La búsqueda de categoría por ID devuelve null
     * - Se lanza CategoryNotFoundException
     * - No se realiza el update
     *
     * Verificaciones:
     * - Se invoca findById de producto y de categoría
     * - NO se invoca update del repositorio de producto
     */
    @Test
    void should_throw_when_category_not_found() {
        Long id = 3L;
        Long missingCategoryId = 99L;
        Product existing = new Product(id, 1L, "Milanesa", "d", new BigDecimal("80.00"), null, 3, true);
        when(productRepositoryPort.findById(id)).thenReturn(existing);
        when(categoryRepositoryPort.findById(missingCategoryId)).thenReturn(null);

        Product input = new Product(null, missingCategoryId, "Milanesa", "d2", new BigDecimal("85.00"), null, 2, true);

        assertThrows(CategoryNotFoundException.class, () -> updateProduct.update(id, input));

        verify(productRepositoryPort, times(1)).findById(id);
        verify(categoryRepositoryPort, times(1)).findById(missingCategoryId);
        verify(productRepositoryPort, never()).update(any());
    }

    /**
     * Test: Omite validación de categoría cuando categoryId es null.
     *
     * Escenario: Se actualiza un producto y no se provee categoría (null), por lo que
     * no debe consultarse al repositorio de categorías.
     *
     * Comportamiento esperado:
     * - Se busca el producto por ID y se encuentra
     * - NO se consulta categoría al ser null
     * - Se realiza el update con categoryId null
     *
     * Verificaciones:
     * - NO se invoca categoryRepositoryPort.findById
     * - Sí se invoca productRepositoryPort.update
     */
    @Test
    void should_skip_category_lookup_when_category_is_null() {
        Long id = 4L;
        Product existing = new Product(id, 5L, "Faina", "d", new BigDecimal("20.00"), null, 10, true);
        when(productRepositoryPort.findById(id)).thenReturn(existing);
        when(productRepositoryPort.update(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product input = new Product(null, null, "Faina", "d2", new BigDecimal("25.00"), null, 8, false);

        Product result = updateProduct.update(id, input);
        assertNotNull(result);
        assertNull(result.getCategoryId());
        verify(categoryRepositoryPort, never()).findById(any());
        verify(productRepositoryPort, times(1)).update(any(Product.class));
    }
}
