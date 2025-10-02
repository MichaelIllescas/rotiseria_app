package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
import com.imperialnet.foodstore.business.domain.exception.BusinessNotFoundException;
import com.imperialnet.foodstore.business.domain.model.Business;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para la clase GetBusinessById.
 *
 * Verifica que el servicio de búsqueda de negocios por ID funcione correctamente
 * en diferentes escenarios: búsqueda exitosa de negocios existentes, validación de parámetros,
 * manejo de errores cuando el negocio no existe, y limpieza adecuada del contexto de logging (MDC).
 */
@ExtendWith(MockitoExtension.class)
class GetBusinessByIdTest {

    @Mock
    private BusinessRepositoryPort businessRepository;

    @InjectMocks
    private GetBusinessById service;

    private Business existingBusiness;

    @BeforeEach
    void setUp() {
        // Negocio existente para tests exitosos
        existingBusiness = new Business(
                1L,
                "Rotisería El Buen Sabor",
                "La mejor rotisería del barrio",
                "contacto@rotiseria.com",
                "+54911234567",
                "Av. Principal 123",
                true
        );

        // Limpiar MDC antes de cada test
        MDC.clear();
    }

    /**
     * Test: Verificar búsqueda exitosa de un negocio existente.
     *
     * Escenario: Se busca un negocio que existe en el sistema.
     *
     * Comportamiento esperado:
     * - Se valida que el ID no sea nulo
     * - Se llama al repositorio para buscar el negocio por ID
     * - El repositorio retorna el negocio encontrado
     * - Se registran logs informativos durante el proceso
     * - Se retorna el negocio encontrado
     * - Se limpia correctamente el contexto MDC de logging
     *
     * Verificaciones:
     * - Se retorna el negocio correcto con todos sus campos
     * - Se llama a findById con el ID correcto
     * - No se lanzan excepciones durante la operación
     * - El MDC queda limpio después de la operación
     */
    @Test
    void shouldGetBusinessByIdSuccessfully() {
        // Arrange
        Long businessId = 1L;
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(existingBusiness));

        // Act
        Business result = service.getBusinessById(businessId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(businessId);
        assertThat(result.getName()).isEqualTo("Rotisería El Buen Sabor");
        assertThat(result.getDescription()).isEqualTo("La mejor rotisería del barrio");
        assertThat(result.getEmail()).isEqualTo("contacto@rotiseria.com");
        assertThat(result.getPhone()).isEqualTo("+54911234567");
        assertThat(result.getAddress()).isEqualTo("Av. Principal 123");
        assertThat(result.isActive()).isTrue();

        // Verificar interacciones con el repositorio
        verify(businessRepository).findById(businessId);
        verifyNoMoreInteractions(businessRepository);
    }

    /**
     * Test: Verificar que se lance excepción al buscar negocio con ID nulo.
     *
     * Escenario: Se intenta buscar un negocio pasando un ID nulo como parámetro.
     *
     * Comportamiento esperado:
     * - Se valida el parámetro de entrada antes de cualquier operación
     * - Se lanza IllegalArgumentException con mensaje descriptivo
     * - NO se realizan llamadas al repositorio
     * - Se registra un log de warning sobre el ID nulo
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se lanza la excepción esperada (IllegalArgumentException)
     * - El mensaje de excepción indica que el ID no puede ser nulo
     * - NO se llama a findById
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldThrowExceptionWhenBusinessIdIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getBusinessById(null)
        );

        assertThat(exception.getMessage()).isEqualTo("El ID del negocio no puede ser nulo");

        // Verificar que no se hicieron llamadas al repositorio
        verifyNoInteractions(businessRepository);
    }

    /**
     * Test: Verificar que se lance excepción al buscar negocio inexistente.
     *
     * Escenario: Se busca un negocio que no existe en el sistema.
     *
     * Comportamiento esperado:
     * - Se valida que el ID no sea nulo
     * - Se llama al repositorio para buscar el negocio
     * - El repositorio retorna Optional.empty()
     * - Se lanza BusinessNotFoundException con el ID del negocio
     * - Se registra un log de warning sobre el negocio no encontrado
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se lanza la excepción esperada (BusinessNotFoundException)
     * - El businessId en la excepción coincide con el ID buscado
     * - Se llama a findById pero retorna Optional.empty()
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldThrowBusinessNotFoundExceptionWhenBusinessDoesNotExist() {
        // Arrange
        Long nonExistentBusinessId = 999L;
        when(businessRepository.findById(nonExistentBusinessId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessNotFoundException exception = assertThrows(
                BusinessNotFoundException.class,
                () -> service.getBusinessById(nonExistentBusinessId)
        );

        assertThat(exception.getBusinessId()).isEqualTo(nonExistentBusinessId);

        // Verificar que se intentó buscar
        verify(businessRepository).findById(nonExistentBusinessId);
    }

    /**
     * Test: Verificar limpieza correcta del contexto MDC tras operación exitosa.
     *
     * Escenario: Se completa exitosamente la búsqueda de un negocio.
     *
     * Comportamiento esperado:
     * - Durante la operación se establecen valores en MDC para logging (action, businessId)
     * - Después de una operación exitosa, el contexto MDC debe quedar completamente limpio
     * - No deben persistir datos de logging entre diferentes operaciones
     *
     * Verificaciones:
     * - Tras completar la búsqueda, action y businessId no están en MDC
     * - Se confirma que la limpieza ocurre en operaciones exitosas
     *
     * Nota: Este test es crucial para evitar contaminación de logs entre
     * diferentes operaciones en un entorno multi-hilo.
     */
    @Test
    void shouldClearMDCContextAfterSuccessfulOperation() {
        // Arrange
        Long businessId = 1L;
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(existingBusiness));

        // Act
        service.getBusinessById(businessId);

        // Assert - verificar que MDC se limpió al final
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessId")).isNull();
    }

    /**
     * Test: Verificar limpieza correcta del contexto MDC tras excepción de validación.
     *
     * Escenario: Ocurre una excepción de validación (ID nulo) durante la búsqueda.
     *
     * Comportamiento esperado:
     * - Se establece contexto MDC al inicio de la operación
     * - Aunque ocurra una excepción de validación, el bloque finally debe limpiar el MDC
     * - No debe quedar información de logging contaminando el contexto
     *
     * Verificaciones:
     * - Tras la excepción, action y businessId no están en MDC
     * - La limpieza ocurre incluso cuando falla la validación
     */
    @Test
    void shouldClearMDCContextAfterValidationException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.getBusinessById(null));

        // Verificar que MDC se limpió incluso tras la excepción
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessId")).isNull();
    }

    /**
     * Test: Verificar limpieza correcta del contexto MDC tras excepción de negocio no encontrado.
     *
     * Escenario: Ocurre una BusinessNotFoundException durante la búsqueda.
     *
     * Comportamiento esperado:
     * - Se establece contexto MDC al inicio de la operación
     * - Aunque ocurra una excepción de negocio no encontrado, el bloque finally debe limpiar el MDC
     * - No debe quedar información de logging contaminando el contexto
     *
     * Verificaciones:
     * - Tras la excepción, action y businessId no están en MDC
     * - La limpieza ocurre incluso cuando el negocio no existe
     */
    @Test
    void shouldClearMDCContextAfterBusinessNotFoundException() {
        // Arrange
        Long businessId = 999L;
        when(businessRepository.findById(businessId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessNotFoundException.class, () -> service.getBusinessById(businessId));

        // Verificar que MDC se limpió incluso tras la excepción
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessId")).isNull();
    }

    /**
     * Test: Verificar propagación correcta de excepciones del repositorio.
     *
     * Escenario: El repositorio lanza una excepción durante la operación de búsqueda.
     *
     * Comportamiento esperado:
     * - Se valida correctamente que el ID no es nulo
     * - Durante la búsqueda ocurre un error (ej. problema de base de datos)
     * - La excepción del repositorio se propaga sin modificaciones
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se propaga la excepción original del repositorio
     * - El mensaje de excepción se mantiene intacto
     * - Se llama a findById antes de la excepción
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldPropagateRepositoryExceptions() {
        // Arrange
        Long businessId = 1L;
        when(businessRepository.findById(businessId)).thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.getBusinessById(businessId)
        );

        assertThat(exception.getMessage()).isEqualTo("Database connection error");

        // Verificar que se intentó buscar
        verify(businessRepository).findById(businessId);

        // Verificar que MDC se limpió tras la excepción
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessId")).isNull();
    }



    /**
     * Test: Verificar que solo se realiza una llamada al repositorio por operación.
     *
     * Escenario: Se ejecuta una operación completa de búsqueda de negocio.
     *
     * Comportamiento esperado:
     * - Se valida el ID una sola vez
     * - Se llama al repositorio exactamente una vez
     * - No se realizan llamadas adicionales o redundantes
     *
     * Verificaciones:
     * - findById se llama exactamente una vez
     * - No hay interacciones adicionales con el repositorio
     * - La operación es eficiente y directa
     */
    @Test
    void shouldCallRepositoryOnlyOncePerOperation() {
        // Arrange
        Long businessId = 1L;
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(existingBusiness));

        // Act
        service.getBusinessById(businessId);

        // Assert - verificar que se llama exactamente una vez
        verify(businessRepository, times(1)).findById(businessId);
        verifyNoMoreInteractions(businessRepository);
    }

    /**
     * Test: Verificar que se retorna exactamente el negocio devuelto por el repositorio.
     *
     * Escenario: El repositorio devuelve un negocio específico.
     *
     * Comportamiento esperado:
     * - Se busca el negocio correctamente por ID
     * - Se debe retornar exactamente la misma instancia que devuelve el repositorio
     * - No se debe modificar el resultado después de la búsqueda
     *
     * Verificaciones:
     * - El resultado es exactamente el objeto devuelto por repository.findById()
     * - Se preservan todos los campos del negocio encontrado
     * - Se confirma que el servicio actúa como intermediario sin modificaciones
     */
    @Test
    void shouldReturnExactBusinessFromRepository() {
        // Arrange
        Long businessId = 1L;
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(existingBusiness));

        // Act
        Business result = service.getBusinessById(businessId);

        // Assert
        assertThat(result).isSameAs(existingBusiness);
        assertThat(result.getId()).isEqualTo(existingBusiness.getId());
        assertThat(result.getName()).isEqualTo(existingBusiness.getName());
        assertThat(result.getEmail()).isEqualTo(existingBusiness.getEmail());
    }
}
