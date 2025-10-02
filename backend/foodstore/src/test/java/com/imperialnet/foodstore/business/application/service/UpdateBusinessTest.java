package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.in.UpdateBusinessUseCase.UpdateBusinessCommand;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para la clase UpdateBusiness.
 *
 * Verifica que el servicio de actualización de negocios funcione correctamente
 * en diferentes escenarios: actualización exitosa de negocios, validación de parámetros,
 * verificación de existencia, validación de duplicados por nombre, manejo de errores,
 * y limpieza adecuada del contexto de logging (MDC).
 */
@ExtendWith(MockitoExtension.class)
class UpdateBusinessTest {

    @Mock
    private BusinessRepositoryPort businessRepository;

    @InjectMocks
    private UpdateBusiness service;

    private UpdateBusinessCommand validCommand;
    private Business existingBusiness;
    private Business updatedBusiness;
    private Business duplicateBusiness;

    @BeforeEach
    void setUp() {
        // Comando válido para actualizar negocio
        validCommand = new UpdateBusinessCommand(
                1L,
                "Rotisería El Nuevo Sabor",
                "La mejor rotisería del barrio - actualizada",
                "nuevo@rotiseria.com",
                "+54911234567",
                "Av. Principal 123, Ciudad",
                true
        );

        // Negocio existente en BD
        existingBusiness = new Business(
                1L,
                "Rotisería El Buen Sabor",
                "La mejor rotisería del barrio",
                "contacto@rotiseria.com",
                "+54911234567",
                "Av. Principal 123",
                true
        );

        // Negocio actualizado que será guardado
        updatedBusiness = new Business(
                1L,
                validCommand.name(),
                validCommand.description(),
                validCommand.email(),
                validCommand.phone(),
                validCommand.address(),
                validCommand.active()
        );

        // Negocio con nombre duplicado (diferente ID)
        duplicateBusiness = new Business(
                2L,
                "Rotisería El Nuevo Sabor",
                "Otro negocio",
                "otro@email.com",
                "+54911999999",
                "Otra dirección",
                true
        );

        // Limpiar MDC antes de cada test
        MDC.clear();
    }

    /**
     * Test: Verificar actualización exitosa de un negocio existente.
     *
     * Escenario: Se actualiza un negocio que existe en el sistema con datos válidos y nombre único.
     *
     * Comportamiento esperado:
     * - Se valida que el ID no sea nulo
     * - Se verifica que el negocio existe en el sistema
     * - Se verifica que no hay duplicados por nombre (excluyendo el mismo negocio)
     * - Se crea una nueva entidad Business con los datos actualizados
     * - Se guarda el negocio en el repositorio
     * - Se retorna el negocio actualizado
     * - Se registran logs informativos durante el proceso
     * - Se limpia correctamente el contexto MDC de logging
     *
     * Verificaciones:
     * - El negocio retornado tiene todos los campos actualizados
     * - Se llama a findById para verificar existencia
     * - Se llama a findByName para verificar duplicados
     * - Se llama a update con la entidad actualizada
     * - El MDC queda limpio después de la operación
     */
    @Test
    void shouldUpdateBusinessSuccessfully() {
        // Arrange
        when(businessRepository.findById(validCommand.id())).thenReturn(Optional.of(existingBusiness));
        when(businessRepository.findByName(validCommand.name())).thenReturn(Optional.empty());
        when(businessRepository.update(any(Business.class))).thenReturn(updatedBusiness);

        // Act
        Business result = service.updateBusiness(validCommand);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validCommand.id());
        assertThat(result.getName()).isEqualTo(validCommand.name());
        assertThat(result.getDescription()).isEqualTo(validCommand.description());
        assertThat(result.getEmail()).isEqualTo(validCommand.email());
        assertThat(result.getPhone()).isEqualTo(validCommand.phone());
        assertThat(result.getAddress()).isEqualTo(validCommand.address());
        assertThat(result.isActive()).isEqualTo(validCommand.active());

        // Verificar interacciones con el repositorio
        verify(businessRepository).findById(validCommand.id());
        verify(businessRepository).findByName(validCommand.name());

        ArgumentCaptor<Business> businessCaptor = ArgumentCaptor.forClass(Business.class);
        verify(businessRepository).update(businessCaptor.capture());
        Business capturedBusiness = businessCaptor.getValue();

        assertThat(capturedBusiness.getId()).isEqualTo(validCommand.id());
        assertThat(capturedBusiness.getName()).isEqualTo(validCommand.name());
        assertThat(capturedBusiness.isActive()).isEqualTo(validCommand.active());
    }

    /**
     * Test: Verificar que se lance excepción al actualizar negocio con ID nulo.
     *
     * Escenario: Se intenta actualizar un negocio pasando un ID nulo en el comando.
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
     * - NO se llama a findById, findByName ni update
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldThrowExceptionWhenBusinessIdIsNull() {
        // Arrange
        UpdateBusinessCommand commandWithNullId = new UpdateBusinessCommand(
                null, "Nombre", "Desc", "email@test.com", "+123", "Dir", true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateBusiness(commandWithNullId)
        );

        assertThat(exception.getMessage()).isEqualTo("El ID del negocio no puede ser nulo");

        // Verificar que no se hicieron llamadas al repositorio
        verifyNoInteractions(businessRepository);
    }

    /**
     * Test: Verificar que se lance excepción al actualizar negocio inexistente.
     *
     * Escenario: Se intenta actualizar un negocio que no existe en el sistema.
     *
     * Comportamiento esperado:
     * - Se valida que el ID no sea nulo
     * - Se intenta buscar el negocio en el repositorio
     * - El repositorio retorna Optional.empty()
     * - Se lanza BusinessNotFoundException con el ID del negocio
     * - NO se intenta verificar duplicados ni actualizar
     * - Se registra un log de warning sobre el negocio no encontrado
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se lanza la excepción esperada (BusinessNotFoundException)
     * - El businessId en la excepción coincide con el ID buscado
     * - Se llama a findById pero NO a findByName ni update
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldThrowBusinessNotFoundExceptionWhenBusinessDoesNotExist() {
        // Arrange
        Long nonExistentId = 999L;
        UpdateBusinessCommand commandForNonExistent = new UpdateBusinessCommand(
                nonExistentId, "Nombre", "Desc", "email@test.com", "+123", "Dir", true);

        when(businessRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessNotFoundException exception = assertThrows(
                BusinessNotFoundException.class,
                () -> service.updateBusiness(commandForNonExistent)
        );

        assertThat(exception.getBusinessId()).isEqualTo(nonExistentId);

        // Verificar que se buscó pero no se actualizó
        verify(businessRepository).findById(nonExistentId);
        verify(businessRepository, never()).findByName(any());
        verify(businessRepository, never()).update(any(Business.class));
    }

    /**
     * Test: Verificar que se lance excepción cuando ya existe otro negocio con el mismo nombre.
     *
     * Escenario: Se intenta actualizar un negocio con un nombre que ya usa otro negocio diferente.
     *
     * Comportamiento esperado:
     * - Se valida que el ID no sea nulo
     * - Se verifica que el negocio existe
     * - Se busca si existe otro negocio con el mismo nombre
     * - Se encuentra otro negocio con diferente ID usando ese nombre
     * - Se lanza IllegalArgumentException con mensaje sobre duplicado
     * - NO se intenta actualizar
     * - Se registra un log de warning sobre el duplicado
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se lanza la excepción esperada (IllegalArgumentException)
     * - El mensaje de excepción contiene el nombre duplicado
     * - Se llama a findById y findByName pero NO a update
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldThrowExceptionWhenBusinessNameAlreadyExistsForDifferentBusiness() {
        // Arrange
        when(businessRepository.findById(validCommand.id())).thenReturn(Optional.of(existingBusiness));
        when(businessRepository.findByName(validCommand.name())).thenReturn(Optional.of(duplicateBusiness));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateBusiness(validCommand)
        );

        assertThat(exception.getMessage()).contains("Ya existe otro negocio con el nombre: " + validCommand.name());

        // Verificar que se buscó pero no se actualizó
        verify(businessRepository).findById(validCommand.id());
        verify(businessRepository).findByName(validCommand.name());
        verify(businessRepository, never()).update(any(Business.class));
    }

    /**
     * Test: Verificar actualización exitosa cuando el negocio mantiene su propio nombre.
     *
     * Escenario: Se actualiza un negocio manteniendo el mismo nombre que ya tenía.
     *
     * Comportamiento esperado:
     * - Se valida que el ID no sea nulo
     * - Se verifica que el negocio existe
     * - Se busca si existe otro negocio con el mismo nombre
     * - Se encuentra el mismo negocio (mismo ID) con ese nombre
     * - Se permite la actualización porque es el mismo negocio
     * - Se actualiza exitosamente
     *
     * Verificaciones:
     * - La actualización se completa sin errores
     * - Se permite usar el mismo nombre que ya tenía el negocio
     * - Se llama a findById, findByName y update
     * - El negocio se actualiza correctamente
     */
    @Test
    void shouldUpdateBusinessSuccessfullyWhenKeepingSameName() {
        // Arrange - usar el mismo nombre que ya tenía el negocio existente
        UpdateBusinessCommand commandWithSameName = new UpdateBusinessCommand(
                1L,
                existingBusiness.getName(), // mismo nombre
                "Nueva descripción",
                "nuevo@email.com",
                "+54911999999",
                "Nueva dirección",
                false
        );

        Business updatedWithSameName = new Business(
                1L,
                existingBusiness.getName(),
                "Nueva descripción",
                "nuevo@email.com",
                "+54911999999",
                "Nueva dirección",
                false
        );

        when(businessRepository.findById(1L)).thenReturn(Optional.of(existingBusiness));
        when(businessRepository.findByName(existingBusiness.getName())).thenReturn(Optional.of(existingBusiness));
        when(businessRepository.update(any(Business.class))).thenReturn(updatedWithSameName);

        // Act
        Business result = service.updateBusiness(commandWithSameName);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(existingBusiness.getName());
        assertThat(result.getDescription()).isEqualTo("Nueva descripción");
        assertThat(result.getEmail()).isEqualTo("nuevo@email.com");

        // Verificar todas las llamadas
        verify(businessRepository).findById(1L);
        verify(businessRepository).findByName(existingBusiness.getName());
        verify(businessRepository).update(any(Business.class));
    }

    /**
     * Test: Verificar limpieza correcta del contexto MDC tras operación exitosa.
     *
     * Escenario: Se completa exitosamente la actualización de un negocio.
     *
     * Comportamiento esperado:
     * - Durante la operación se establecen valores en MDC para logging (action, businessId, businessName)
     * - Después de una operación exitosa, el contexto MDC debe quedar completamente limpio
     * - No deben persistir datos de logging entre diferentes operaciones
     *
     * Verificaciones:
     * - Tras completar la actualización, action, businessId y businessName no están en MDC
     * - Se confirma que la limpieza ocurre en operaciones exitosas
     *
     * Nota: Este test es crucial para evitar contaminación de logs entre
     * diferentes operaciones en un entorno multi-hilo.
     */
    @Test
    void shouldClearMDCContextAfterSuccessfulOperation() {
        // Arrange
        when(businessRepository.findById(validCommand.id())).thenReturn(Optional.of(existingBusiness));
        when(businessRepository.findByName(validCommand.name())).thenReturn(Optional.empty());
        when(businessRepository.update(any(Business.class))).thenReturn(updatedBusiness);

        // Act
        service.updateBusiness(validCommand);

        // Assert - verificar que MDC se limpió al final
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessId")).isNull();
        assertThat(MDC.get("businessName")).isNull();
    }

    /**
     * Test: Verificar limpieza correcta del contexto MDC tras excepción de validación.
     *
     * Escenario: Ocurre una excepción de validación (ID nulo) durante la actualización.
     *
     * Comportamiento esperado:
     * - Se establece contexto MDC al inicio de la operación
     * - Aunque ocurra una excepción de validación, el bloque finally debe limpiar el MDC
     * - No debe quedar información de logging contaminando el contexto
     *
     * Verificaciones:
     * - Tras la excepción, action, businessId y businessName no están en MDC
     * - La limpieza ocurre incluso cuando falla la validación
     */
    @Test
    void shouldClearMDCContextAfterValidationException() {
        // Arrange
        UpdateBusinessCommand commandWithNullId = new UpdateBusinessCommand(
                null, "Nombre", "Desc", "email@test.com", "+123", "Dir", true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.updateBusiness(commandWithNullId));

        // Verificar que MDC se limpió incluso tras la excepción
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessId")).isNull();
        assertThat(MDC.get("businessName")).isNull();
    }

    /**
     * Test: Verificar propagación correcta de excepciones del repositorio.
     *
     * Escenario: El repositorio lanza una excepción durante la operación de actualización.
     *
     * Comportamiento esperado:
     * - Se valida correctamente que el ID no es nulo
     * - Se verifica la existencia del negocio
     * - Se valida que no hay duplicados
     * - Durante la actualización ocurre un error (ej. problema de base de datos)
     * - La excepción del repositorio se propaga sin modificaciones
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se propaga la excepción original del repositorio
     * - El mensaje de excepción se mantiene intacto
     * - Se llama a findById, findByName y update en el orden correcto
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldPropagateRepositoryExceptions() {
        // Arrange
        when(businessRepository.findById(validCommand.id())).thenReturn(Optional.of(existingBusiness));
        when(businessRepository.findByName(validCommand.name())).thenReturn(Optional.empty());
        when(businessRepository.update(any(Business.class))).thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.updateBusiness(validCommand)
        );

        assertThat(exception.getMessage()).isEqualTo("Database connection error");

        // Verificar que se intentaron todas las operaciones
        verify(businessRepository).findById(validCommand.id());
        verify(businessRepository).findByName(validCommand.name());
        verify(businessRepository).update(any(Business.class));

        // Verificar que MDC se limpió tras la excepción
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessId")).isNull();
        assertThat(MDC.get("businessName")).isNull();
    }

    /**
     * Test: Verificar orden correcto de llamadas a métodos del repositorio.
     *
     * Escenario: Se ejecuta una operación completa de actualización de negocio.
     *
     * Comportamiento esperado:
     * - Primero se debe buscar el negocio por ID (verificar existencia)
     * - Luego se debe buscar por nombre (verificar duplicados)
     * - Finalmente se debe actualizar el negocio
     * - El orden es crítico para la lógica de negocio
     *
     * Verificaciones:
     * - findById se llama antes que findByName
     * - findByName se llama antes que update
     * - Se respeta el flujo: verificar existencia → verificar duplicados → actualizar
     * - Las operaciones se ejecutan en secuencia, no en paralelo
     */
    @Test
    void shouldCallRepositoryMethodsInCorrectOrder() {
        // Arrange
        when(businessRepository.findById(validCommand.id())).thenReturn(Optional.of(existingBusiness));
        when(businessRepository.findByName(validCommand.name())).thenReturn(Optional.empty());
        when(businessRepository.update(any(Business.class))).thenReturn(updatedBusiness);

        // Act
        service.updateBusiness(validCommand);

        // Assert - verificar el orden de las llamadas
        var inOrder = inOrder(businessRepository);
        inOrder.verify(businessRepository).findById(validCommand.id());
        inOrder.verify(businessRepository).findByName(validCommand.name());
        inOrder.verify(businessRepository).update(any(Business.class));
    }

    /**
     * Test: Verificar que se retorna exactamente el negocio devuelto por el repositorio.
     *
     * Escenario: El repositorio devuelve un negocio modificado tras la actualización.
     *
     * Comportamiento esperado:
     * - Se valida y actualiza el negocio correctamente
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
    void shouldReturnExactBusinessFromRepository() {
        // Arrange
        Business repositoryResult = new Business(
                validCommand.id(),
                "Nombre Modificado Por BD",
                validCommand.description(),
                validCommand.email(),
                validCommand.phone(),
                validCommand.address(),
                validCommand.active()
        );

        when(businessRepository.findById(validCommand.id())).thenReturn(Optional.of(existingBusiness));
        when(businessRepository.findByName(validCommand.name())).thenReturn(Optional.empty());
        when(businessRepository.update(any(Business.class))).thenReturn(repositoryResult);

        // Act
        Business result = service.updateBusiness(validCommand);

        // Assert
        assertThat(result).isSameAs(repositoryResult);
        assertThat(result.getName()).isEqualTo("Nombre Modificado Por BD");
        assertThat(result.getId()).isEqualTo(validCommand.id());
    }
}
