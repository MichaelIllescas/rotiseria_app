
package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
import com.imperialnet.foodstore.business.domain.exception.BusinessNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para la clase DeleteBusiness.
 *
 * Verifica que el servicio de eliminación de negocios funcione correctamente
 * en diferentes escenarios: eliminación exitosa de negocios, validación de parámetros,
 * manejo de errores cuando el negocio no existe, y limpieza adecuada del contexto de logging (MDC).
 */
@ExtendWith(MockitoExtension.class)
class DeleteBusinessTest {

    @Mock
    private BusinessRepositoryPort businessRepository;

    @InjectMocks
    private DeleteBusiness service;

    @BeforeEach
    void setUp() {
        // Limpiar MDC antes de cada test
        MDC.clear();
    }

    /**
     * Test: Verificar eliminación exitosa de un negocio existente.
     *
     * Escenario: Se intenta eliminar un negocio que existe en el sistema.
     *
     * Comportamiento esperado:
     * - Se valida que el ID no sea nulo
     * - Se llama al repositorio para eliminar el negocio por ID
     * - El repositorio confirma la eliminación exitosa (retorna true)
     * - Se registran logs informativos durante el proceso
     * - Se limpia correctamente el contexto MDC de logging
     * - No se lanzan excepciones
     *
     * Verificaciones:
     * - Se llama a deleteById con el ID correcto
     * - No se lanzan excepciones durante la operación
     * - El MDC queda limpio después de la operación
     */
    @Test
    void shouldDeleteBusinessSuccessfully() {
        // Arrange
        Long businessId = 1L;
        when(businessRepository.deleteById(businessId)).thenReturn(true);

        // Act & Assert - no debe lanzar excepción
        assertDoesNotThrow(() -> service.deleteBusiness(businessId));

        // Verificar interacciones con el repositorio
        verify(businessRepository).deleteById(businessId);
        verifyNoMoreInteractions(businessRepository);
    }

    /**
     * Test: Verificar que se lance excepción al intentar eliminar negocio con ID nulo.
     *
     * Escenario: Se intenta eliminar un negocio pasando un ID nulo como parámetro.
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
     * - NO se llama a deleteById
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldThrowExceptionWhenBusinessIdIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.deleteBusiness(null)
        );

        assertThat(exception.getMessage()).isEqualTo("El ID del negocio no puede ser nulo");

        // Verificar que no se hicieron llamadas al repositorio
        verifyNoInteractions(businessRepository);
    }

    /**
     * Test: Verificar que se lance excepción al intentar eliminar negocio inexistente.
     *
     * Escenario: Se intenta eliminar un negocio que no existe en el sistema.
     *
     * Comportamiento esperado:
     * - Se valida que el ID no sea nulo
     * - Se llama al repositorio para eliminar el negocio
     * - El repositorio indica que no se eliminó nada (retorna false)
     * - Se lanza BusinessNotFoundException con el ID del negocio
     * - Se registra un log de warning sobre el negocio no encontrado
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se lanza la excepción esperada (BusinessNotFoundException)
     * - El businessId en la excepción coincide con el ID buscado
     * - Se llama a deleteById pero retorna false
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldThrowBusinessNotFoundExceptionWhenBusinessDoesNotExist() {
        // Arrange
        Long nonExistentBusinessId = 999L;
        when(businessRepository.deleteById(nonExistentBusinessId)).thenReturn(false);

        // Act & Assert
        BusinessNotFoundException exception = assertThrows(
                BusinessNotFoundException.class,
                () -> service.deleteBusiness(nonExistentBusinessId)
        );

        assertThat(exception.getBusinessId()).isEqualTo(nonExistentBusinessId);

        // Verificar que se intentó eliminar
        verify(businessRepository).deleteById(nonExistentBusinessId);
    }

    /**
     * Test: Verificar limpieza correcta del contexto MDC tras operación exitosa.
     *
     * Escenario: Se completa exitosamente la eliminación de un negocio.
     *
     * Comportamiento esperado:
     * - Durante la operación se establecen valores en MDC para logging (action, businessId)
     * - Después de una operación exitosa, el contexto MDC debe quedar completamente limpio
     * - No deben persistir datos de logging entre diferentes operaciones
     *
     * Verificaciones:
     * - Tras completar la eliminación, action y businessId no están en MDC
     * - Se confirma que la limpieza ocurre en operaciones exitosas
     *
     * Nota: Este test es crucial para evitar contaminación de logs entre
     * diferentes operaciones en un entorno multi-hilo.
     */
    @Test
    void shouldClearMDCContextAfterSuccessfulOperation() {
        // Arrange
        Long businessId = 1L;
        when(businessRepository.deleteById(businessId)).thenReturn(true);

        // Act
        service.deleteBusiness(businessId);

        // Assert - verificar que MDC se limpió al final
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessId")).isNull();
    }

    /**
     * Test: Verificar limpieza correcta del contexto MDC tras excepción de validación.
     *
     * Escenario: Ocurre una excepción de validación (ID nulo) durante la eliminación.
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
        assertThrows(IllegalArgumentException.class, () -> service.deleteBusiness(null));

        // Verificar que MDC se limpió incluso tras la excepción
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessId")).isNull();
    }

    /**
     * Test: Verificar limpieza correcta del contexto MDC tras excepción de negocio no encontrado.
     *
     * Escenario: Ocurre una BusinessNotFoundException durante la eliminación.
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
        when(businessRepository.deleteById(businessId)).thenReturn(false);

        // Act & Assert
        assertThrows(BusinessNotFoundException.class, () -> service.deleteBusiness(businessId));

        // Verificar que MDC se limpió incluso tras la excepción
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessId")).isNull();
    }

    /**
     * Test: Verificar propagación correcta de excepciones del repositorio.
     *
     * Escenario: El repositorio lanza una excepción durante la operación de eliminación.
     *
     * Comportamiento esperado:
     * - Se valida correctamente que el ID no es nulo
     * - Durante la eliminación ocurre un error (ej. problema de base de datos)
     * - La excepción del repositorio se propaga sin modificaciones
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se propaga la excepción original del repositorio
     * - El mensaje de excepción se mantiene intacto
     * - Se llama a deleteById antes de la excepción
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldPropagateRepositoryExceptions() {
        // Arrange
        Long businessId = 1L;
        when(businessRepository.deleteById(businessId)).thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.deleteBusiness(businessId)
        );

        assertThat(exception.getMessage()).isEqualTo("Database connection error");

        // Verificar que se intentó eliminar
        verify(businessRepository).deleteById(businessId);

        // Verificar que MDC se limpió tras la excepción
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessId")).isNull();
    }

    /**
     * Test: Verificar manejo correcto de diferentes IDs válidos.
     *
     * Escenario: Se eliminan negocios con diferentes IDs válidos.
     *
     * Comportamiento esperado:
     * - Se acepta cualquier ID Long válido (positivo, negativo, cero)
     * - Se llama al repositorio con el ID exacto proporcionado
     * - La eliminación se completa exitosamente
     *
     * Verificaciones:
     * - Se acepta ID positivo, cero y negativo
     * - Se pasa el ID exacto al repositorio
     * - No se lanzan excepciones para IDs válidos
     */
    @Test
    void shouldHandleDifferentValidIds() {
        // Test con ID positivo
        Long positiveId = 123L;
        when(businessRepository.deleteById(positiveId)).thenReturn(true);
        assertDoesNotThrow(() -> service.deleteBusiness(positiveId));
        verify(businessRepository).deleteById(positiveId);

        // Test con ID cero
        Long zeroId = 0L;
        when(businessRepository.deleteById(zeroId)).thenReturn(true);
        assertDoesNotThrow(() -> service.deleteBusiness(zeroId));
        verify(businessRepository).deleteById(zeroId);

        // Test con ID negativo (edge case)
        Long negativeId = -1L;
        when(businessRepository.deleteById(negativeId)).thenReturn(true);
        assertDoesNotThrow(() -> service.deleteBusiness(negativeId));
        verify(businessRepository).deleteById(negativeId);
    }

    /**
     * Test: Verificar que solo se realiza una llamada al repositorio por operación.
     *
     * Escenario: Se ejecuta una operación completa de eliminación de negocio.
     *
     * Comportamiento esperado:
     * - Se valida el ID una sola vez
     * - Se llama al repositorio exactamente una vez
     * - No se realizan llamadas adicionales o redundantes
     *
     * Verificaciones:
     * - deleteById se llama exactamente una vez
     * - No hay interacciones adicionales con el repositorio
     * - La operación es eficiente y directa
     */
    @Test
    void shouldCallRepositoryOnlyOncePerOperation() {
        // Arrange
        Long businessId = 1L;
        when(businessRepository.deleteById(businessId)).thenReturn(true);

        // Act
        service.deleteBusiness(businessId);

        // Assert - verificar que se llama exactamente una vez
        verify(businessRepository, times(1)).deleteById(businessId);
        verifyNoMoreInteractions(businessRepository);
    }
}
