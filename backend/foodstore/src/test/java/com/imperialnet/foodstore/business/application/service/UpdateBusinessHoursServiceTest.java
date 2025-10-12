package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.out.BusinessHourRepositoryPort;
import com.imperialnet.foodstore.business.domain.model.BusinessHour;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBusinessHoursServiceTest {

    @Mock
    private BusinessHourRepositoryPort repository;

    @InjectMocks
    private UpdateBusinessHoursService service;

    private List<BusinessHour> sampleHours;

    @BeforeEach
    void setUp() {
        // Construir una lista de horarios de ejemplo con dos días
        BusinessHour monday = new BusinessHour(
                1L,
                "Lunes",
                true,
                Arrays.asList(
                        new BusinessHour.Range("09:00", "12:00"),
                        new BusinessHour.Range("16:00", "20:00")
                )
        );

        BusinessHour tuesday = new BusinessHour(
                2L,
                "Martes",
                true,
                Arrays.asList(
                        new BusinessHour.Range("10:00", "13:30"),
                        new BusinessHour.Range("17:00", "21:00")
                )
        );

        sampleHours = Arrays.asList(monday, tuesday);
    }

    @Test
    void shouldUpdateAllBusinessHoursSuccessfully() {
        // Act
        service.updateAll(sampleHours);

        // Assert
        verify(repository, times(1)).saveAll(sampleHours);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldHandleEmptyList() {
        // Arrange
        List<BusinessHour> empty = Collections.emptyList();

        // Act
        service.updateAll(empty);

        // Assert
        verify(repository, times(1)).saveAll(empty);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldPropagateRepositoryExceptions() {
        // Arrange
        doThrow(new RuntimeException("DB failure")).when(repository).saveAll(anyList());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateAll(sampleHours));
        assertThat(ex.getMessage()).isEqualTo("DB failure");

        verify(repository, times(1)).saveAll(sampleHours);
        verifyNoMoreInteractions(repository);
    }
}

