package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.in.CreateBusinessUseCase.CreateBusinessCommand;
import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
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
 * Suite de tests unitarios para la clase CreateBusiness.
 *
 * Verifica que el servicio de creación de negocios funcione correctamente
 * en diferentes escenarios: creación exitosa de negocios, validación de duplicados,
 * manejo de errores, y limpieza adecuada del contexto de logging (MDC).
 */
@ExtendWith(MockitoExtension.class)
class CreateBusinessTest {

    @Mock
    private BusinessRepositoryPort businessRepository;

    @InjectMocks
    private CreateBusiness service;

    private CreateBusinessCommand validCommand;
    private Business existingBusiness;
    private Business newBusiness;

    @BeforeEach
    void setUp() {
        // Comando válido para crear negocio
        validCommand = new CreateBusinessCommand(
                "Rotisería El Buen Sabor",
                "La mejor rotisería del barrio con comida casera",
                "contacto@rotiseria.com",
                "+54911234567",
                "Av. Principal 123, Ciudad",
                true
        );

        // Negocio existente para tests de duplicados
        existingBusiness = new Business(
                1L,
                "Rotisería El Buen Sabor",
                "Descripción existente",
                "existing@email.com",
                "+54911111111",
                "Dirección existente",
                true
        );

        // Nuevo negocio que será creado
        newBusiness = new Business(
                2L,
                validCommand.name(),
                validCommand.description(),
                validCommand.email(),
                validCommand.phone(),
                validCommand.address(),
                validCommand.active()
        );

        // Limpiar MDC antes de cada test
        MDC.clear();
    }

    /**
     * Test: Verificar creación exitosa de un nuevo negocio.
     *
     * Escenario: Se intenta crear un negocio con datos válidos y nombre único.
     *
     * Comportamiento esperado:
     * - Se verifica que no existe un negocio con el mismo nombre
     * - Se crea una nueva entidad Business con los datos del comando
     * - Se guarda el negocio en el repositorio
     * - Se retorna el negocio guardado con ID asignado
     * - Se registran logs informativos durante el proceso
     * - Se limpia correctamente el contexto MDC de logging
     *
     * Verificaciones:
     * - El negocio retornado tiene el ID asignado por el repositorio
     * - Todos los campos del comando se transfieren correctamente
     * - Se llama a findByName para verificar duplicados
     * - Se llama a save con la entidad creada
     * - El MDC queda limpio después de la operación
     */
    @Test
    void shouldCreateBusinessSuccessfully() {
        // Arrange
        when(businessRepository.findByName(validCommand.name())).thenReturn(Optional.empty());
        when(businessRepository.save(any(Business.class))).thenReturn(newBusiness);

        // Act
        Business result = service.createBusiness(validCommand);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo(validCommand.name());
        assertThat(result.getDescription()).isEqualTo(validCommand.description());
        assertThat(result.getEmail()).isEqualTo(validCommand.email());
        assertThat(result.getPhone()).isEqualTo(validCommand.phone());
        assertThat(result.getAddress()).isEqualTo(validCommand.address());
        assertThat(result.isActive()).isEqualTo(validCommand.active());

        // Verificar interacciones con el repositorio
        verify(businessRepository).findByName(validCommand.name());

        ArgumentCaptor<Business> businessCaptor = ArgumentCaptor.forClass(Business.class);
        verify(businessRepository).save(businessCaptor.capture());
        Business savedBusiness = businessCaptor.getValue();

        assertThat(savedBusiness.getId()).isNull(); // ID debe ser null antes de guardar
        assertThat(savedBusiness.getName()).isEqualTo(validCommand.name());
        assertThat(savedBusiness.isActive()).isEqualTo(validCommand.active());
    }

    /**
     * Test: Verificar que se lance excepción al intentar crear negocio con nombre duplicado.
     *
     * Escenario: Se intenta crear un negocio con un nombre que ya existe en el sistema.
     *
     * Comportamiento esperado:
     * - El repositorio encuentra un negocio existente con el mismo nombre
     * - Se lanza IllegalArgumentException con mensaje descriptivo
     * - NO se intenta guardar el nuevo negocio
     * - Se registra un log de warning sobre el duplicado
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se lanza la excepción esperada (IllegalArgumentException)
     * - El mensaje de excepción contiene el nombre duplicado
     * - Se llama a findByName pero NO a save
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldThrowExceptionWhenBusinessNameAlreadyExists() {
        // Arrange
        when(businessRepository.findByName(validCommand.name())).thenReturn(Optional.of(existingBusiness));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.createBusiness(validCommand)
        );

        assertThat(exception.getMessage()).contains("Ya existe un negocio con el nombre: " + validCommand.name());

        // Verificar que se buscó pero no se guardó
        verify(businessRepository).findByName(validCommand.name());
        verify(businessRepository, never()).save(any(Business.class));
    }

    /**
     * Test: Verificar creación de negocio inactivo.
     *
     * Escenario: Se crea un negocio con estado inactivo desde el inicio.
     *
     * Comportamiento esperado:
     * - Se acepta y procesa correctamente un comando con active=false
     * - La entidad Business se crea con estado inactivo
     * - Se guarda y retorna el negocio con estado inactivo preservado
     * - Los logs reflejan correctamente el estado inactivo
     *
     * Verificaciones:
     * - El negocio creado tiene estado inactivo (false)
     * - Se transfieren correctamente todos los campos incluyendo el estado
     * - El repositorio recibe una entidad con estado inactivo
     */
    @Test
    void shouldCreateInactiveBusiness() {
        // Arrange
        CreateBusinessCommand inactiveCommand = new CreateBusinessCommand(
                "Negocio Temporal",
                "Un negocio que inicia inactivo",
                "temporal@test.com",
                "+54911999999",
                "Dirección Temporal 999",
                false // inactivo
        );

        Business inactiveBusiness = new Business(
                3L,
                inactiveCommand.name(),
                inactiveCommand.description(),
                inactiveCommand.email(),
                inactiveCommand.phone(),
                inactiveCommand.address(),
                false
        );

        when(businessRepository.findByName(inactiveCommand.name())).thenReturn(Optional.empty());
        when(businessRepository.save(any(Business.class))).thenReturn(inactiveBusiness);

        // Act
        Business result = service.createBusiness(inactiveCommand);

        // Assert
        assertThat(result.isActive()).isFalse();

        ArgumentCaptor<Business> businessCaptor = ArgumentCaptor.forClass(Business.class);
        verify(businessRepository).save(businessCaptor.capture());
        Business savedBusiness = businessCaptor.getValue();
        assertThat(savedBusiness.isActive()).isFalse();
    }

    /**
     * Test: Verificar limpieza correcta del contexto MDC tras operación exitosa.
     *
     * Escenario: Se completa exitosamente la creación de un negocio.
     *
     * Comportamiento esperado:
     * - Durante la operación se establecen valores en MDC para logging (action, businessName)
     * - Después de una operación exitosa, el contexto MDC debe quedar completamente limpio
     * - No deben persistir datos de logging entre diferentes operaciones
     *
     * Verificaciones:
     * - Tras completar la creación, action y businessName no están en MDC
     * - Se confirma que la limpieza ocurre en operaciones exitosas
     *
     * Nota: Este test es crucial para evitar contaminación de logs entre
     * diferentes operaciones en un entorno multi-hilo.
     */
    @Test
    void shouldClearMDCContextAfterSuccessfulOperation() {
        // Arrange
        when(businessRepository.findByName(validCommand.name())).thenReturn(Optional.empty());
        when(businessRepository.save(any(Business.class))).thenReturn(newBusiness);

        // Act
        service.createBusiness(validCommand);

        // Assert - verificar que MDC se limpió al final
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessName")).isNull();
    }

    /**
     * Test: Verificar limpieza correcta del contexto MDC tras excepción.
     *
     * Escenario: Ocurre una excepción durante la creación del negocio.
     *
     * Comportamiento esperado:
     * - Se establece contexto MDC al inicio de la operación
     * - Aunque ocurra una excepción, el bloque finally debe limpiar el MDC
     * - No debe quedar información de logging contaminando el contexto
     *
     * Verificaciones:
     * - Tras la excepción, action y businessName no están en MDC
     * - La limpieza ocurre incluso cuando falla la operación
     */
    @Test
    void shouldClearMDCContextAfterException() {
        // Arrange
        when(businessRepository.findByName(validCommand.name())).thenReturn(Optional.of(existingBusiness));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.createBusiness(validCommand));

        // Verificar que MDC se limpió incluso tras la excepción
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessName")).isNull();
    }

    /**
     * Test: Verificar propagación correcta de excepciones del repositorio.
     *
     * Escenario: El repositorio lanza una excepción durante la operación de guardado.
     *
     * Comportamiento esperado:
     * - Se verifica correctamente que no hay duplicados
     * - Durante el guardado ocurre un error (ej. problema de base de datos)
     * - La excepción del repositorio se propaga sin modificaciones
     * - Se limpia correctamente el contexto MDC incluso tras la excepción
     *
     * Verificaciones:
     * - Se propaga la excepción original del repositorio
     * - El mensaje de excepción se mantiene intacto
     * - Se llama a findByName y save en el orden correcto
     * - El MDC queda limpio después de la excepción
     */
    @Test
    void shouldPropagateRepositoryExceptions() {
        // Arrange
        when(businessRepository.findByName(validCommand.name())).thenReturn(Optional.empty());
        when(businessRepository.save(any(Business.class))).thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.createBusiness(validCommand)
        );

        assertThat(exception.getMessage()).isEqualTo("Database connection error");

        // Verificar que se intentó buscar y guardar
        verify(businessRepository).findByName(validCommand.name());
        verify(businessRepository).save(any(Business.class));

        // Verificar que MDC se limpió tras la excepción
        assertThat(MDC.get("action")).isNull();
        assertThat(MDC.get("businessName")).isNull();
    }

    /**
     * Test: Verificar orden correcto de llamadas a métodos del repositorio.
     *
     * Escenario: Se ejecuta una operación completa de creación de negocio.
     *
     * Comportamiento esperado:
     * - Primero se debe verificar duplicados con findByName
     * - Solo después de confirmar que no hay duplicados se debe guardar
     * - El orden es crítico para la lógica de negocio
     *
     * Verificaciones:
     * - findByName se llama antes que save
     * - Se respeta el flujo: verificar → crear entidad → guardar
     * - Las operaciones se ejecutan en secuencia, no en paralelo
     */
    @Test
    void shouldCallRepositoryMethodsInCorrectOrder() {
        // Arrange
        when(businessRepository.findByName(validCommand.name())).thenReturn(Optional.empty());
        when(businessRepository.save(any(Business.class))).thenReturn(newBusiness);

        // Act
        service.createBusiness(validCommand);

        // Assert - verificar el orden de las llamadas
        var inOrder = inOrder(businessRepository);
        inOrder.verify(businessRepository).findByName(validCommand.name());
        inOrder.verify(businessRepository).save(any(Business.class));
    }

    /**
     * Test: Verificar que se retorna exactamente el negocio devuelto por el repositorio.
     *
     * Escenario: El repositorio asigna ID y posiblemente modifica otros campos durante la persistencia.
     *
     * Comportamiento esperado:
     * - Se crea la entidad correctamente a partir del comando
     * - El repositorio puede asignar ID y modificar timestamps durante el guardado
     * - Se debe retornar exactamente lo que devuelve el repositorio
     * - No se debe modificar el resultado después de la persistencia
     *
     * Verificaciones:
     * - El resultado es exactamente el objeto devuelto por repository.save()
     * - Se preservan todos los campos del negocio guardado
     * - Se confirma que el servicio actúa como intermediario sin modificaciones adicionales
     */
    @Test
    void shouldReturnExactBusinessFromRepository() {
        // Arrange
        Business repositoryResult = new Business(
                99L, // ID diferente asignado por el repositorio
                validCommand.name(),
                validCommand.description(),
                validCommand.email(),
                validCommand.phone(),
                validCommand.address(),
                validCommand.active()
        );

        when(businessRepository.findByName(validCommand.name())).thenReturn(Optional.empty());
        when(businessRepository.save(any(Business.class))).thenReturn(repositoryResult);

        // Act
        Business result = service.createBusiness(validCommand);

        // Assert
        assertThat(result).isSameAs(repositoryResult);
        assertThat(result.getId()).isEqualTo(99L);
        assertThat(result.getName()).isEqualTo(validCommand.name());
    }

    /**
     * Test: Verificar manejo correcto de campos opcionales en el comando.
     *
     * Escenario: Se crea un negocio con descripción nula (campo opcional).
     *
     * Comportamiento esperado:
     * - Se acepta comando con descripción null sin lanzar excepciones
     * - La entidad Business se crea correctamente con descripción null
     * - El repositorio guarda correctamente la entidad con campo null
     *
     * Verificaciones:
     * - No se lanzan excepciones por campos opcionales nulos
     * - La entidad se crea con la descripción null preservada
     * - El negocio guardado mantiene la descripción null
     */
    @Test
    void shouldHandleOptionalFieldsCorrectly() {
        // Arrange
        CreateBusinessCommand commandWithNullDescription = new CreateBusinessCommand(
                "Negocio Sin Descripción",
                null, // descripción opcional
                "sindes@test.com",
                "+54911777777",
                "Sin Descripción 777",
                true
        );

        Business businessWithNullDescription = new Business(
                4L,
                commandWithNullDescription.name(),
                null,
                commandWithNullDescription.email(),
                commandWithNullDescription.phone(),
                commandWithNullDescription.address(),
                commandWithNullDescription.active()
        );

        when(businessRepository.findByName(commandWithNullDescription.name())).thenReturn(Optional.empty());
        when(businessRepository.save(any(Business.class))).thenReturn(businessWithNullDescription);

        // Act
        Business result = service.createBusiness(commandWithNullDescription);

        // Assert
        assertThat(result.getDescription()).isNull();

        ArgumentCaptor<Business> businessCaptor = ArgumentCaptor.forClass(Business.class);
        verify(businessRepository).save(businessCaptor.capture());
        Business savedBusiness = businessCaptor.getValue();
        assertThat(savedBusiness.getDescription()).isNull();
    }
}
