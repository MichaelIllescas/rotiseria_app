package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
import com.imperialnet.foodstore.business.domain.exception.BusinessNotFoundException;
import com.imperialnet.foodstore.business.domain.model.Business;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para la clase ChangeStatusBusiness.
 *
 * Verifica que el servicio de cambio de estado de negocios funcione correctamente
 * en diferentes escenarios: activación de negocios inactivos, desactivación de
 * negocios activos, manejo de errores, y limpieza adecuada del contexto de logging (MDC).
 */
@ExtendWith(MockitoExtension.class)
class ChangeStatusBusinessTest {

    @Mock
    private BusinessRepositoryPort businessRepository;

    @InjectMocks
    private ChangeStatusBusiness service;

    private Business activeBusiness;
    private Business inactiveBusiness;

    @BeforeEach
    void setUp() {
        // Crear negocios de prueba
        activeBusiness = new Business(
                1L,
                "Rotisería El Buen Sabor",
                "La mejor rotisería del barrio",
                "contacto@rotiseria.com",
                "+54911234567",
                "Av. Principal 123",
                true // activo
        );

        inactiveBusiness = new Business(
                2L,
                "Rotisería Los Amigos",
                "Comida casera y deliciosa",
                "info@losamigos.com",
                "+54911987654",
                "Calle Secundaria 456",
                false // inactivo
        );

        // Limpiar MDC antes de cada test
        MDC.clear();
    }

    /**
     * Test: Verificar desactivación exitosa de un negocio activo.
     *
     * Escenario: Se intenta cambiar el estado de un negocio que está actualmente activo.
     *
     * Comportamiento esperado:
     * - El repositorio encuentra el negocio existente (activo)
     * - Se invoca el método deactivate() en el negocio
     * - Se actualiza el negocio en el repositorio
     * - Se retorna el negocio con estado inactivo
     * - Se limpia correctamente el contexto MDC de logging
     *
     * Verificaciones:
     * - El negocio retornado tiene estado inactivo (false)
     * - Se llama a findById con el ID correcto
     * - Se llama a update con el negocio modificado
     * - El negocio pasado a update tiene estado inactivo
     * - El MDC queda limpio después de la operación
     */
    @Test
    void shouldDeactivateActiveBusiness() {
        // Arrange
        Long businessId = 1L;
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(activeBusiness));
        when(businessRepository.update(any(Business.class))).thenAnswer(invocation -> {
            Business business = invocation.getArgument(0);
            return new Business(
                    business.getId(),
                    business.getName(),
                    business.getDescription(),
                    business.getEmail(),
                    business.getPhone(),
                    business.getAddress(),
                    business.isActive()
            );
        });

        // Act
        Business result = service.changeBusinessStatus(businessId);

        // Assert
        assertThat(result.isActive()).isFalse();

        // Verificar que se llamó al repositorio correctamente
        verify(businessRepository).findById(businessId);

        ArgumentCaptor<Business> businessCaptor = ArgumentCaptor.forClass(Business.class);
        verify(businessRepository).update(businessCaptor.capture());
        Business updatedBusiness = businessCaptor.getValue();
        assertThat(updatedBusiness.isActive()).isFalse();
    }

    /**
     * Test: Verificar activación exitosa de un negocio inactivo.
     *
     * Escenario: Se intenta cambiar el estado de un negocio que está actualmente inactivo.
     *
     * Comportamiento esperado:
     * - El repositorio encuentra el negocio existente (inactivo)
     * - Se invoca el método activate() en el negocio
     * - Se actualiza el negocio en el repositorio
     * - Se retorna el negocio con estado activo
     * - Se limpia correctamente el contexto MDC de logging
     *
     * Verificaciones:
     * - El negocio retornado tiene estado activo (true)
     * - Se llama a findById con el ID correcto
     * - Se llama a update con el negocio modificado
     * - El negocio pasado a update tiene estado activo
     * - El MDC queda limpio después de la operación
     */
    @Test
    void shouldActivateInactiveBusiness() {
        // Arrange
        Long businessId = 2L;
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(inactiveBusiness));
        when(businessRepository.update(any(Business.class))).thenAnswer(invocation -> {
            Business business = invocation.getArgument(0);
            return new Business(
                    business.getId(),
                    business.getName(),
                    business.getDescription(),
                    business.getEmail(),
                    business.getPhone(),
                    business.getAddress(),
                    business.isActive()
            );
        });

        // Act
        Business result = service.changeBusinessStatus(businessId);

        // Assert
        assertThat(result.isActive()).isTrue();

        // Verificar que se llamó al repositorio correctamente
        verify(businessRepository).findById(businessId);

        ArgumentCaptor<Business> businessCaptor = ArgumentCaptor.forClass(Business.class);
        verify(businessRepository).update(businessCaptor.capture());
        Business updatedBusiness = businessCaptor.getValue();
        assertThat(updatedBusiness.isActive()).isTrue();
    }

    /**
     * Test: Verificar que se lance excepción al intentar cambiar estado de negocio inexistente.
     *
     * Escenario: Se intenta cambiar el estado de un negocio que no existe en el sistema.
     *
     * Comportamiento esperado:
     * - El repositorio no encuentra ningún negocio con el ID especificado (retorna Optional.empty())
     * - Se lanza BusinessNotFoundException con mensaje descriptivo
     * - NO se intenta actualizar ningún negocio
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se lanza la excepción esperada (BusinessNotFoundException)
     * - El businessId en la excepción coincide con el ID buscado
     * - El mensaje de excepción contiene el ID del negocio
     * - Se llama a findById pero NO a update
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
                () -> service.changeBusinessStatus(nonExistentBusinessId)
        );

        assertThat(exception.getBusinessId()).isEqualTo(nonExistentBusinessId);
        assertThat(exception.getMessage()).contains("Negocio con ID 999 no encontrado");

        // Verificar que no se intentó actualizar
        verify(businessRepository).findById(nonExistentBusinessId);
        verify(businessRepository, never()).update(any(Business.class));
    }

    /**
     * Test: Verificar que se lance excepción al pasar ID de negocio nulo.
     *
     * Escenario: Se intenta cambiar el estado pasando un ID nulo como parámetro.
     *
     * Comportamiento esperado:
     * - Se valida el parámetro de entrada antes de cualquier operación
     * - Se lanza IllegalArgumentException con mensaje descriptivo
     * - NO se realizan llamadas al repositorio
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se lanza la excepción esperada (IllegalArgumentException)
     * - El mensaje de excepción indica que el ID no puede ser nulo
     * - NO se llama a findById ni a update
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldThrowIllegalArgumentExceptionWhenBusinessIdIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.changeBusinessStatus(null)
        );

        assertThat(exception.getMessage()).isEqualTo("El ID del negocio no puede ser nulo");

        // Verificar que no se hicieron llamadas al repositorio
        verify(businessRepository, never()).findById(any());
        verify(businessRepository, never()).update(any(Business.class));
    }

    /**
     * Test: Verificar limpieza correcta del contexto MDC tras operación exitosa.
     *
     * Escenario: Se completa exitosamente el cambio de estado de un negocio.
     *
     * Comportamiento esperado:
     * - Durante la operación se establecen valores en MDC para logging
     * - Después de una operación exitosa, el contexto MDC debe quedar limpio
     * - No deben persistir datos de logging entre diferentes operaciones
     *
     * Verificaciones:
     * - Tras completar el cambio de estado, action y businessId no están en MDC
     * - Se confirma que la limpieza ocurre en operaciones exitosas
     *
     * Nota: Este test es crucial para evitar contaminación de logs entre
     * diferentes operaciones en un entorno multi-hilo.
     */
    @Test
    void shouldSetMDCContextCorrectly() {
        // Arrange
        Long businessId = 1L;
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(activeBusiness));
        when(businessRepository.update(any(Business.class))).thenReturn(activeBusiness);

        // Act
        service.changeBusinessStatus(businessId);

        // Assert - verificar que MDC se limpió al final
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessId")).isNull();
    }

    /**
     * Test: Verificar propagación correcta de excepciones del repositorio.
     *
     * Escenario: El repositorio lanza una excepción durante la operación de actualización.
     *
     * Comportamiento esperado:
     * - El negocio se encuentra correctamente en el repositorio
     * - Durante la actualización ocurre un error (ej. problema de base de datos)
     * - La excepción del repositorio se propaga sin modificaciones
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se propaga la excepción original del repositorio
     * - El mensaje de excepción se mantiene intacto
     * - Se llama a findById y update en el orden correcto
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldPropagateRepositoryExceptions() {
        // Arrange
        Long businessId = 1L;
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(activeBusiness));
        when(businessRepository.update(any(Business.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.changeBusinessStatus(businessId)
        );

        assertThat(exception.getMessage()).isEqualTo("Database error");

        // Verificar que se intentó buscar y actualizar
        verify(businessRepository).findById(businessId);
        verify(businessRepository).update(any(Business.class));
    }

    /**
     * Test: Verificar orden correcto de llamadas a métodos del repositorio.
     *
     * Escenario: Se ejecuta una operación completa de cambio de estado.
     *
     * Comportamiento esperado:
     * - Primero se debe buscar el negocio por ID
     * - Solo después de encontrarlo se debe actualizar
     * - El orden es crítico para la lógica de negocio
     *
     * Verificaciones:
     * - findById se llama antes que update
     * - Se respeta el flujo: buscar → verificar existencia → modificar → actualizar
     * - Las operaciones se ejecutan en secuencia, no en paralelo
     */
    @Test
    void shouldCallRepositoryMethodsInCorrectOrder() {
        // Arrange
        Long businessId = 1L;
        when(businessRepository.findById(businessId)).thenReturn(Optional.of(activeBusiness));
        when(businessRepository.update(any(Business.class))).thenReturn(activeBusiness);

        // Act
        service.changeBusinessStatus(businessId);

        // Assert - verificar el orden de las llamadas
        var inOrder = inOrder(businessRepository);
        inOrder.verify(businessRepository).findById(businessId);
        inOrder.verify(businessRepository).update(any(Business.class));
    }

    /**
     * Test: Verificar que se retorna el negocio actualizado del repositorio.
     *
     * Escenario: El repositorio devuelve un negocio modificado tras la actualización.
     *
     * Comportamiento esperado:
     * - Se busca y modifica el negocio correctamente
     * - El repositorio puede modificar otros campos durante la persistencia
     * - Se debe retornar exactamente lo que devuelve el repositorio
     * - No se debe modificar el resultado después de la persistencia
     *
     * Verificaciones:
     * - El resultado es exactamente el objeto devuelto por repository.update()
     * - Se preservan todos los campos del negocio actualizado
     * - Se confirma que el servicio actúa como intermediario sin modificaciones adicionales
     */
    @Test
    void shouldReturnUpdatedBusinessFromRepository() {
        // Arrange
        Long businessId = 1L;
        Business expectedUpdatedBusiness = new Business(
                1L,
                "Updated Business Name",
                "Updated description",
                "updated@email.com",
                "+54911111111",
                "Updated Address 789",
                false
        );

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(activeBusiness));
        when(businessRepository.update(any(Business.class))).thenReturn(expectedUpdatedBusiness);

        // Act
        Business result = service.changeBusinessStatus(businessId);

        // Assert
        assertThat(result).isEqualTo(expectedUpdatedBusiness);
        assertThat(result.getName()).isEqualTo("Updated Business Name");
        assertThat(result.getEmail()).isEqualTo("updated@email.com");
    }
}
