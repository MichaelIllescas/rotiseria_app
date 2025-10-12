package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
import com.imperialnet.foodstore.business.domain.model.Business;
import com.imperialnet.foodstore.users.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para GetCurrentBussinesService.
 *
 * Casos cubiertos:
 * - Retorna el primer negocio activo disponible.
 * - Lanza BusinessException cuando no hay negocios activos.
 * - Con múltiples activos, devuelve el primero en el orden de la lista.
 */
@ExtendWith(MockitoExtension.class)
class GetCurrentBussinesServiceTest {

    @Mock
    private BusinessRepositoryPort businessRepositoryPort;

    @InjectMocks
    private GetCurrentBussinesService service;

    private Business buildBusiness(long id, String name, boolean active) {
        return new Business(
                id,
                name,
                "Descripción " + name,
                name.toLowerCase().replace(" ","") + "@example.com",
                "+54 911 0000-0000",
                "Calle Falsa 123",
                active
        );
    }

    @Test
    void shouldReturnFirstActiveBusiness_whenListContainsActive() {
        // Arrange: primero inactivo, luego activo
        Business inactive = buildBusiness(3L, "Inactivo", false);
        Business active = buildBusiness(5L, "Activo", true);
        when(businessRepositoryPort.findAll()).thenReturn(Arrays.asList(inactive, active));

        // Act
        Business current = service.getCurrentBusiness();

        // Assert
        assertThat(current).isNotNull();
        assertThat(current.getId()).isEqualTo(5L);
        assertThat(current.isActive()).isTrue();

        verify(businessRepositoryPort, times(1)).findAll();
        verifyNoMoreInteractions(businessRepositoryPort);
    }

    @Test
    void shouldThrowBusinessException_whenNoActiveBusinessInList() {
        // Arrange: lista vacía
        when(businessRepositoryPort.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> service.getCurrentBusiness());
        assertThat(ex.getMessage()).isEqualTo("No hay ningún negocio activo");

        verify(businessRepositoryPort, times(1)).findAll();
        verifyNoMoreInteractions(businessRepositoryPort);
    }

    @Test
    void shouldReturnFirstActive_whenMultipleActive() {
        // Arrange: múltiples activos, debe devolver el primero encontrado
        Business firstActive = buildBusiness(7L, "Primero Activo", true);
        Business secondActive = buildBusiness(8L, "Segundo Activo", true);
        when(businessRepositoryPort.findAll()).thenReturn(Arrays.asList(firstActive, secondActive));

        // Act
        Business current = service.getCurrentBusiness();

        // Assert
        assertThat(current).isNotNull();
        assertThat(current.getId()).isEqualTo(7L);
        assertThat(current.isActive()).isTrue();

        verify(businessRepositoryPort, times(1)).findAll();
        verifyNoMoreInteractions(businessRepositoryPort);
    }
}

