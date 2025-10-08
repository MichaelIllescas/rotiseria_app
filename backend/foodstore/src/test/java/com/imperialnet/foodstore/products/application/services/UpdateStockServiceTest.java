package com.imperialnet.foodstore.products.application.services;

import com.imperialnet.foodstore.products.application.ports.out.ProductRepositoryPort;
import com.imperialnet.foodstore.products.domain.exception.ProductNotFoundException;
import com.imperialnet.foodstore.products.domain.model.Product;
import com.imperialnet.foodstore.users.domain.exception.BusinessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para UpdateStockService.
 *
 * Casos cubiertos:
 * - Actualización exitosa del stock (con valor positivo)
 * - Producto inexistente
 * - Stock negativo (regla de dominio lanza IllegalArgumentException)
 * - Stock cero permitido
 *
 * Documentado al estilo de CreateProductTest para mantener consistencia.
 */
@ExtendWith(MockitoExtension.class)
class UpdateStockServiceTest {

    @Mock
    private ProductRepositoryPort productRepository;

    private UpdateStockService updateStockService;

    @BeforeEach
    void setUp() {
        updateStockService = new UpdateStockService(productRepository);
    }

    @AfterEach
    void cleanup() {
        MDC.clear(); // Seguridad extra si alguna prueba falla
    }

    /**
     * Test: Actualización exitosa del stock.
     *
     * Escenario:
     * - El producto existe y se solicita actualizar su stock a un valor positivo.
     *
     * Comportamiento esperado:
     * - Se busca el producto por ID, se encuentra
     * - Se invoca resetDailyStock con el nuevo stock
     * - Se persiste el cambio con save
     * - Se limpia el MDC al finalizar
     */
    @Test
    void deberiaActualizarStockExitosamente() {
        Long productId = 10L;
        int newStock = 25;
        Product existing = new Product(productId, 3L, "Pizza", "desc", new BigDecimal("100.00"), "img", 5, true);
        when(productRepository.findById(productId)).thenReturn(existing);
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        updateStockService.updateStock(productId, newStock);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product saved = captor.getValue();
        assertEquals(newStock, saved.getDailyStock());

        verify(productRepository).findById(productId);
        assertNull(MDC.get("action"));
        assertNull(MDC.get("productId"));
        assertNull(MDC.get("productName"));
        assertNull(MDC.get("categoryId"));
    }

    /**
     * Test: Falla si el producto no existe.
     *
     * Escenario:
     * - Se intenta actualizar el stock de un ID inexistente.
     *
     * Comportamiento esperado:
     * - Se lanza ProductNotFoundException con mensaje descriptivo
     * - No se invoca save
     * - Se limpia el MDC
     */
    @Test
    void deberiaLanzarExcepcionSiProductoNoExiste() {
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(null);

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class,
                () -> updateStockService.updateStock(productId, 10));
        assertTrue(ex.getMessage().contains("Producto no encontrado con ID: "));

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any());
        assertNull(MDC.get("action"));
        assertNull(MDC.get("productId"));
    }

    /**
     * Test: Falla con stock negativo (regla de dominio).
     *
     * Escenario:
     * - El producto existe pero el nuevo stock es negativo.
     *
     * Comportamiento esperado:
     * - resetDailyStock lanza IllegalArgumentException
     * - No se invoca save
     * - El MDC se limpia
     */
    @Test
    void deberiaLanzarExcepcionSiStockNegativo() {
        Long productId = 20L;
        int newStock = -1;
        Product existing = new Product(productId, 4L, "Empanada", "desc", new BigDecimal("50.00"), "img2", 10, true);
        when(productRepository.findById(productId)).thenReturn(existing);

        assertThrows(BusinessException.class, () -> updateStockService.updateStock(productId, newStock));
        verify(productRepository, never()).save(any());
        assertNull(MDC.get("action"));
        assertNull(MDC.get("productId"));
    }

    /**
     * Test: Stock cero permitido.
     *
     * Escenario:
     * - El producto existe y se actualiza el stock a 0.
     *
     * Comportamiento esperado:
     * - No se lanza excepción
     * - Se persiste el cambio con dailyStock = 0
     */
    @Test
    void deberiaPermitirStockCero() {
        Long productId = 30L;
        int newStock = 0;
        Product existing = new Product(productId, 6L, "Milanesa", "desc", new BigDecimal("200.00"), "img3", 8, true);
        when(productRepository.findById(productId)).thenReturn(existing);
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        updateStockService.updateStock(productId, newStock);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product saved = captor.getValue();
        assertEquals(0, saved.getDailyStock());
    }
}

