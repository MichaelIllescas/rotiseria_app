package com.imperialnet.foodstore.business.domain.model;

import com.imperialnet.foodstore.users.domain.exception.BusinessException;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Entidad de dominio que representa el horario de atención de un día específico del negocio.
 * Se valida a sí misma en el momento de creación o modificación.
 */
public class BusinessHour {

    private Long id;
    private String dayOfWeek;
    private boolean enabled;
    private List<Range> ranges;

    private static final List<String> VALID_DAYS = List.of(
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    );

    // ✅ Constructor público requerido por MapStruct (sin validación)
    public BusinessHour(Long id, String dayOfWeek, boolean enabled, List<Range> ranges) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.enabled = enabled;
        this.ranges = ranges != null ? ranges : new ArrayList<>();
    }

    // ✅ Constructor privado con validación (usado por el método of)
    private BusinessHour(Long id, String dayOfWeek, boolean enabled, List<Range> ranges, boolean validate) {
        this.id = id;
        this.dayOfWeek = validateDay(dayOfWeek);
        this.enabled = enabled;
        this.ranges = enabled ? validateRanges(ranges) : List.of();
    }

    // ✅ Factory Method con validación
    public static BusinessHour of(Long id, String dayOfWeek, boolean enabled, List<Range> ranges) {
        return new BusinessHour(id, dayOfWeek, enabled, ranges, true);
    }

    // ---------------------------------------------------
    // Validaciones internas
    // ---------------------------------------------------
    private String validateDay(String day) {
        if (day == null || !VALID_DAYS.contains(day))
            throw new IllegalArgumentException("Día inválido: " + day);
        return day;
    }

    private List<Range> validateRanges(List<Range> ranges) {
        if (ranges == null || ranges.isEmpty())
            throw new BusinessException("Debe especificar al menos un rango horario.");

        List<Range> sorted = new ArrayList<>(ranges);
        sorted.sort(Comparator.comparing(Range::getOpenAsTime));

        for (int i = 0; i < sorted.size(); i++) {
            Range current = sorted.get(i);
            if (!current.isValid())
                throw new BusinessException("Rango horario inválido: " + current);

            if (i > 0) {
                Range prev = sorted.get(i - 1);
                if (!current.startsAfter(prev))
                    throw new BusinessException("Rangos solapados o desordenados: " + prev + " y " + current);
            }
        }

        return Collections.unmodifiableList(sorted);
    }

    // ---------------------------------------------------
    // Getters y Setters (MapStruct necesita setters públicos)
    // ---------------------------------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public List<Range> getRanges() { return ranges; }
    public void setRanges(List<Range> ranges) { this.ranges = ranges; }

    @Override
    public String toString() {
        return "BusinessHour{" +
                "dayOfWeek='" + dayOfWeek + '\'' +
                ", enabled=" + enabled +
                ", ranges=" + ranges +
                '}';
    }

    // ---------------------------------------------------
    // Clase interna Range
    // ---------------------------------------------------
    public static class Range {
        private String open;
        private String close;

        private static final Pattern TIME_PATTERN = Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d$");

        public Range() {} // 👈 MapStruct necesita constructor vacío

        public Range(String open, String close) {
            this.open = open;
            this.close = close;
        }

        public boolean isValid() {
            if (open == null || close == null)
                return false;
            if (!TIME_PATTERN.matcher(open).matches() || !TIME_PATTERN.matcher(close).matches())
                return false;
            try {
                LocalTime start = LocalTime.parse(open);
                LocalTime end = LocalTime.parse(close);
                return start.isBefore(end);
            } catch (DateTimeParseException e) {
                return false;
            }
        }

        public boolean startsAfter(Range other) {
            return getOpenAsTime().isAfter(other.getCloseAsTime());
        }

        public LocalTime getOpenAsTime() { return LocalTime.parse(open); }
        public LocalTime getCloseAsTime() { return LocalTime.parse(close); }

        public String getOpen() { return open; }
        public void setOpen(String open) { this.open = open; }

        public String getClose() { return close; }
        public void setClose(String close) { this.close = close; }

        @Override
        public String toString() { return open + " - " + close; }
    }
}
