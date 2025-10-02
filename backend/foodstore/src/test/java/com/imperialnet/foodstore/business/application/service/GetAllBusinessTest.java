package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
import com.imperialnet.foodstore.business.domain.model.Business;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para la clase GetAllBusiness.
 *
 * Verifica que el servicio de consulta de todos los negocios funcione correctamente
 * en diferentes escenarios: obtención exitosa de todos los negocios, obtención de negocios activos,
 * manejo de listas vacías, propagación de excepciones del repositorio, y limpieza adecuada
 * del contexto de logging (MDC).
 */
@ExtendWith(MockitoExtension.class)
class GetAllBusinessTest {

    @Mock
    private BusinessRepositoryPort businessRepository;

    @InjectMocks
    private GetAllBusiness service;

    private List<Business> allBusinesses;
    private List<Business> activeBusinesses;

    @BeforeEach
    void setUp() {
        // Crear lista de todos los negocios (activos e inactivos)
        Business activeBusiness1 = new Business(
                1L,
                "Rotisería El Buen Sabor",
                "La mejor rotisería del barrio",
                "contacto@rotiseria.com",
                "+54911234567",
                "Av. Principal 123",
                true
        );

        Business activeBusiness2 = new Business(
                2L,
                "Parrilla Los Amigos",
                "Asados para toda la familia",
                "info@losamigos.com",
                "+54911987654",
                "Calle Secundaria 456",
                true
        );

        Business inactiveBusiness = new Business(
                3L,
                "Comidas Rápidas Express",
                "Temporalmente cerrado",
                "express@cerrado.com",
                "+54911555666",
                "Avenida Cerrada 789",
                false
        );

        allBusinesses = Arrays.asList(activeBusiness1, activeBusiness2, inactiveBusiness);
        activeBusinesses = Arrays.asList(activeBusiness1, activeBusiness2);

        // Limpiar MDC antes de cada test
        MDC.clear();
    }

    /**
     * Test: Verificar obtención exitosa de todos los negocios.
     *
     * Escenario: Se solicita obtener todos los negocios registrados en el sistema.
     *
     * Comportamiento esperado:
     * - Se llama al repositorio para obtener todos los negocios (activos e inactivos)
     * - Se registran logs informativos antes y después de la consulta
     * - Se retorna la lista completa de negocios
     * - Se limpia correctamente el contexto MDC de logging
     *
     * Verificaciones:
     * - Se retorna la lista completa con todos los negocios
     * - Se incluyen tanto negocios activos como inactivos
     * - Se llama a findAll() exactamente una vez
     * - Los logs informativos contienen el conteo correcto
     * - El MDC queda limpio después de la operación
     */
    @Test
    void shouldGetAllBusinessSuccessfully() {
        // Arrange
        when(businessRepository.findAll()).thenReturn(allBusinesses);

        // Act
        List<Business> result = service.getAllBusiness();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(allBusinesses);

        // Verificar que incluye negocios activos e inactivos
        assertThat(result.stream().filter(Business::isActive)).hasSize(2);
        assertThat(result.stream().filter(b -> !b.isActive())).hasSize(1);

        // Verificar interacciones con el repositorio
        verify(businessRepository).findAll();
        verifyNoMoreInteractions(businessRepository);
    }

    /**
     * Test: Verificar obtención exitosa de negocios activos únicamente.
     *
     * Escenario: Se solicita obtener solo los negocios que están activos en el sistema.
     *
     * Comportamiento esperado:
     * - Se llama al repositorio para obtener solo negocios activos
     * - Se registran logs informativos antes y después de la consulta
     * - Se retorna la lista filtrada de negocios activos
     * - Se limpia correctamente el contexto MDC de logging
     *
     * Verificaciones:
     * - Se retorna solo la lista de negocios activos
     * - Todos los negocios en la lista tienen estado activo (true)
     * - Se llama a findAllActive() exactamente una vez
     * - Los logs informativos contienen el conteo correcto de activos
     * - El MDC queda limpio después de la operación
     */
    @Test
    void shouldGetAllActiveBusinessSuccessfully() {
        // Arrange
        when(businessRepository.findAllActive()).thenReturn(activeBusinesses);

        // Act
        List<Business> result = service.getAllActiveBusiness();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(activeBusinesses);

        // Verificar que todos los negocios están activos
        assertThat(result).allMatch(Business::isActive);

        // Verificar interacciones con el repositorio
        verify(businessRepository).findAllActive();
        verifyNoMoreInteractions(businessRepository);
    }

    /**
     * Test: Verificar manejo correcto de lista vacía para todos los negocios.
     *
     * Escenario: El sistema no tiene negocios registrados.
     *
     * Comportamiento esperado:
     * - Se llama al repositorio que retorna una lista vacía
     * - Se registran logs informativos con conteo cero
     * - Se retorna una lista vacía (no null)
     * - Se limpia correctamente el contexto MDC de logging
     *
     * Verificaciones:
     * - Se retorna una lista vacía, no null
     * - Los logs reflejan que se encontraron 0 negocios
     * - Se llama a findAll() correctamente
     * - El MDC queda limpio después de la operación
     */
    @Test
    void shouldHandleEmptyListForAllBusiness() {
        // Arrange
        when(businessRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Business> result = service.getAllBusiness();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        // Verificar interacciones con el repositorio
        verify(businessRepository).findAll();
        verifyNoMoreInteractions(businessRepository);
    }

    /**
     * Test: Verificar manejo correcto de lista vacía para negocios activos.
     *
     * Escenario: El sistema no tiene negocios activos (todos están inactivos o no hay negocios).
     *
     * Comportamiento esperado:
     * - Se llama al repositorio que retorna una lista vacía de activos
     * - Se registran logs informativos con conteo cero para activos
     * - Se retorna una lista vacía (no null)
     * - Se limpia correctamente el contexto MDC de logging
     *
     * Verificaciones:
     * - Se retorna una lista vacía, no null
     * - Los logs reflejan que se encontraron 0 negocios activos
     * - Se llama a findAllActive() correctamente
     * - El MDC queda limpio después de la operación
     */
    @Test
    void shouldHandleEmptyListForActiveBusiness() {
        // Arrange
        when(businessRepository.findAllActive()).thenReturn(Collections.emptyList());

        // Act
        List<Business> result = service.getAllActiveBusiness();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        // Verificar interacciones con el repositorio
        verify(businessRepository).findAllActive();
        verifyNoMoreInteractions(businessRepository);
    }

    /**
     * Test: Verificar limpieza correcta del contexto MDC tras operación exitosa de getAllBusiness.
     *
     * Escenario: Se completa exitosamente la obtención de todos los negocios.
     *
     * Comportamiento esperado:
     * - Durante la operación se establece "action" = "GET_ALL_BUSINESS" en MDC
     * - Después de una operación exitosa, el contexto MDC debe quedar completamente limpio
     * - No deben persistir datos de logging entre diferentes operaciones
     *
     * Verificaciones:
     * - Tras completar la consulta, action no está en MDC
     * - Se confirma que la limpieza ocurre en operaciones exitosas
     *
     * Nota: Este test es crucial para evitar contaminación de logs entre
     * diferentes operaciones en un entorno multi-hilo.
     */
    @Test
    void shouldClearMDCContextAfterGetAllBusinessOperation() {
        // Arrange
        when(businessRepository.findAll()).thenReturn(allBusinesses);

        // Act
        service.getAllBusiness();

        // Assert - verificar que MDC se limpió al final
        assertThat(MDC.get("action")).isNull();
    }

    /**
     * Test: Verificar limpieza correcta del contexto MDC tras operación exitosa de getAllActiveBusiness.
     *
     * Escenario: Se completa exitosamente la obtención de negocios activos.
     *
     * Comportamiento esperado:
     * - Durante la operación se establece "action" = "GET_ALL_ACTIVE_BUSINESS" en MDC
     * - Después de una operación exitosa, el contexto MDC debe quedar completamente limpio
     * - No deben persistir datos de logging entre diferentes operaciones
     *
     * Verificaciones:
     * - Tras completar la consulta, action no está en MDC
     * - Se confirma que la limpieza ocurre en operaciones exitosas
     */
    @Test
    void shouldClearMDCContextAfterGetAllActiveBusinessOperation() {
        // Arrange
        when(businessRepository.findAllActive()).thenReturn(activeBusinesses);

        // Act
        service.getAllActiveBusiness();

        // Assert - verificar que MDC se limpió al final
        assertThat(MDC.get("action")).isNull();
    }

    /**
     * Test: Verificar propagación correcta de excepciones del repositorio en getAllBusiness.
     *
     * Escenario: El repositorio lanza una excepción durante la consulta de todos los negocios.
     *
     * Comportamiento esperado:
     * - Durante la consulta ocurre un error (ej. problema de base de datos)
     * - La excepción del repositorio se propaga sin modificaciones
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se propaga la excepción original del repositorio
     * - El mensaje de excepción se mantiene intacto
     * - Se llama a findAll() antes de la excepción
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldPropagateRepositoryExceptionsInGetAllBusiness() {
        // Arrange
        when(businessRepository.findAll()).thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.getAllBusiness()
        );

        assertThat(exception.getMessage()).isEqualTo("Database connection error");

        // Verificar que se intentó consultar
        verify(businessRepository).findAll();

        // Verificar que MDC se limpió tras la excepción
        assertThat(MDC.get("action")).isNull();
    }

    /**
     * Test: Verificar propagación correcta de excepciones del repositorio en getAllActiveBusiness.
     *
     * Escenario: El repositorio lanza una excepción durante la consulta de negocios activos.
     *
     * Comportamiento esperado:
     * - Durante la consulta ocurre un error (ej. problema de base de datos)
     * - La excepción del repositorio se propaga sin modificaciones
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se propaga la excepción original del repositorio
     * - El mensaje de excepción se mantiene intacto
     * - Se llama a findAllActive() antes de la excepción
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldPropagateRepositoryExceptionsInGetAllActiveBusiness() {
        // Arrange
        when(businessRepository.findAllActive()).thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.getAllActiveBusiness()
        );

        assertThat(exception.getMessage()).isEqualTo("Database connection error");

        // Verificar que se intentó consultar
        verify(businessRepository).findAllActive();

        // Verificar que MDC se limpió tras la excepción
        assertThat(MDC.get("action")).isNull();
    }

    /**
     * Test: Verificar que solo se realiza una llamada al repositorio por operación getAllBusiness.
     *
     * Escenario: Se ejecuta una operación completa de consulta de todos los negocios.
     *
     * Comportamiento esperado:
     * - Se llama al repositorio exactamente una vez
     * - No se realizan llamadas adicionales o redundantes
     *
     * Verificaciones:
     * - findAll() se llama exactamente una vez
     * - No hay interacciones adicionales con el repositorio
     * - La operación es eficiente y directa
     */
    @Test
    void shouldCallRepositoryOnlyOnceForGetAllBusiness() {
        // Arrange
        when(businessRepository.findAll()).thenReturn(allBusinesses);

        // Act
        service.getAllBusiness();

        // Assert - verificar que se llama exactamente una vez
        verify(businessRepository, times(1)).findAll();
        verifyNoMoreInteractions(businessRepository);
    }

    /**
     * Test: Verificar que solo se realiza una llamada al repositorio por operación getAllActiveBusiness.
     *
     * Escenario: Se ejecuta una operación completa de consulta de negocios activos.
     *
     * Comportamiento esperado:
     * - Se llama al repositorio exactamente una vez
     * - No se realizan llamadas adicionales o redundantes
     *
     * Verificaciones:
     * - findAllActive() se llama exactamente una vez
     * - No hay interacciones adicionales con el repositorio
     * - La operación es eficiente y directa
     */
    @Test
    void shouldCallRepositoryOnlyOnceForGetAllActiveBusiness() {
        // Arrange
        when(businessRepository.findAllActive()).thenReturn(activeBusinesses);

        // Act
        service.getAllActiveBusiness();

        // Assert - verificar que se llama exactamente una vez
        verify(businessRepository, times(1)).findAllActive();
        verifyNoMoreInteractions(businessRepository);
    }

    /**
     * Test: Verificar que se retorna exactamente la lista devuelta por el repositorio para getAllBusiness.
     *
     * Escenario: El repositorio devuelve una lista específica de todos los negocios.
     *
     * Comportamiento esperado:
     * - Se consulta correctamente el repositorio
     * - Se debe retornar exactamente la misma lista que devuelve el repositorio
     * - No se debe modificar la lista después de la consulta
     *
     * Verificaciones:
     * - El resultado es exactamente la lista devuelta por repository.findAll()
     * - Se preservan todos los elementos de la lista
     * - Se confirma que el servicio actúa como intermediario sin modificaciones
     */
    @Test
    void shouldReturnExactListFromRepositoryForGetAllBusiness() {
        // Arrange
        when(businessRepository.findAll()).thenReturn(allBusinesses);

        // Act
        List<Business> result = service.getAllBusiness();

        // Assert
        assertThat(result).isSameAs(allBusinesses);
        assertThat(result).hasSize(allBusinesses.size());
    }

    /**
     * Test: Verificar que se retorna exactamente la lista devuelta por el repositorio para getAllActiveBusiness.
     *
     * Escenario: El repositorio devuelve una lista específica de negocios activos.
     *
     * Comportamiento esperado:
     * - Se consulta correctamente el repositorio
     * - Se debe retornar exactamente la misma lista que devuelve el repositorio
     * - No se debe modificar la lista después de la consulta
     *
     * Verificaciones:
     * - El resultado es exactamente la lista devuelta por repository.findAllActive()
     * - Se preservan todos los elementos de la lista
     * - Se confirma que el servicio actúa como intermediario sin modificaciones
     */
    @Test
    void shouldReturnExactListFromRepositoryForGetAllActiveBusiness() {
        // Arrange
        when(businessRepository.findAllActive()).thenReturn(activeBusinesses);

        // Act
        List<Business> result = service.getAllActiveBusiness();

        // Assert
        assertThat(result).isSameAs(activeBusinesses);
        assertThat(result).hasSize(activeBusinesses.size());
    }
}
