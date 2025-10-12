package com.imperialnet.foodstore.business.domain.model;

import com.imperialnet.foodstore.users.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BusinessHourTest {

    @Test
    void shouldCreateValidBusinessHourAndSortRanges() {
        // Arrange
        List<BusinessHour.Range> ranges = new ArrayList<>();
        ranges.add(new BusinessHour.Range("16:00", "20:00"));
        ranges.add(new BusinessHour.Range("09:00", "12:00")); // desordenado intencionalmente

        // Act
        BusinessHour bh = BusinessHour.of(1L, "Lunes", true, ranges);

        // Assert
        assertThat(bh.getId()).isEqualTo(1L);
        assertThat(bh.getDayOfWeek()).isEqualTo("Lunes");
        assertThat(bh.isEnabled()).isTrue();
        assertThat(bh.getRanges()).hasSize(2);
        // Deben estar ordenados por hora de apertura ascendente
        assertThat(bh.getRanges().get(0).getOpen()).isEqualTo("09:00");
        assertThat(bh.getRanges().get(1).getOpen()).isEqualTo("16:00");

        // La lista devuelta debe ser inmodificable
        assertThrows(UnsupportedOperationException.class, () -> bh.getRanges().add(new BusinessHour.Range("21:00", "22:00")));
    }

    @Test
    void shouldThrowWhenInvalidDay() {
        // Arrange
        List<BusinessHour.Range> ranges = List.of(new BusinessHour.Range("09:00", "12:00"));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> BusinessHour.of(1L, "Funday", true, ranges));
        assertThat(ex.getMessage()).isEqualTo("Día inválido: Funday");
    }

    @Test
    void shouldThrowWhenEnabledTrueAndNoRanges() {
        // Rango vacío
        BusinessException ex1 = assertThrows(BusinessException.class,
                () -> BusinessHour.of(1L, "Lunes", true, List.of()));
        assertThat(ex1.getMessage()).contains("Debe especificar al menos un rango horario.");

        // Rango null
        BusinessException ex2 = assertThrows(BusinessException.class,
                () -> BusinessHour.of(1L, "Lunes", true, null));
        assertThat(ex2.getMessage()).contains("Debe especificar al menos un rango horario.");
    }

    @Test
    void shouldClearRangesWhenDisabled() {
        // Arrange
        List<BusinessHour.Range> ranges = Arrays.asList(
                new BusinessHour.Range("09:00", "12:00"),
                new BusinessHour.Range("16:00", "20:00")
        );

        // Act
        BusinessHour bh = BusinessHour.of(2L, "Martes", false, ranges);

        // Assert: si está deshabilitado, no deben existir rangos
        assertThat(bh.isEnabled()).isFalse();
        assertThat(bh.getRanges()).isEmpty();
    }

    @Test
    void shouldThrowWhenRangeIsInvalid() {
        // close antes de open (inválido)
        List<BusinessHour.Range> invalid = List.of(new BusinessHour.Range("12:00", "10:00"));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> BusinessHour.of(3L, "Miércoles", true, invalid));
        assertThat(ex.getMessage()).contains("Rango horario inválido");
    }

    @Test
    void shouldThrowOnOverlappingRanges() {
        // 11:00-13:00 se solapa con 09:00-12:00
        List<BusinessHour.Range> overlapping = Arrays.asList(
                new BusinessHour.Range("09:00", "12:00"),
                new BusinessHour.Range("11:00", "13:00")
        );

        BusinessException ex = assertThrows(BusinessException.class,
                () -> BusinessHour.of(4L, "Jueves", true, overlapping));
        assertThat(ex.getMessage()).contains("Rangos solapados o desordenados");
    }
}

