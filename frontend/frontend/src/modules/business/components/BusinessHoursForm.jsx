import React, { useState } from "react";

const days = [
  "Lunes",
  "Martes",
  "Miércoles",
  "Jueves",
  "Viernes",
  "Sábado",
  "Domingo",
];

export const BusinessHoursForm = ({ onSubmit }) => {
  const [formData, setFormData] = useState(
    days.map((day) => ({
      dayOfWeek: day,
      enabled: false,
      ranges: [], // inicialmente vacío
    }))
  );

  const handleToggle = (index) => {
    const updated = [...formData];
    updated[index].enabled = !updated[index].enabled;

    // Si se habilita el día, se agrega un rango por defecto
    if (updated[index].enabled && updated[index].ranges.length === 0) {
      updated[index].ranges = [{ open: "11:00", close: "15:00" }];
    }

    // Si se deshabilita, se limpia el arreglo de rangos
    if (!updated[index].enabled) {
      updated[index].ranges = [];
    }

    setFormData(updated);
  };

  const handleChange = (index, field, value) => {
    const updated = [...formData];
    if (updated[index].ranges.length === 0) {
      updated[index].ranges = [{ open: "", close: "" }];
    }
    updated[index].ranges[0][field] = value;
    setFormData(updated);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Limpieza: asegurar que los días cerrados manden "ranges": []
    const cleaned = formData.map((day) => ({
      ...day,
      ranges: day.enabled
        ? day.ranges.filter(
            (r) => r.open.trim() !== "" && r.close.trim() !== ""
          )
        : [],
    }));

    onSubmit(cleaned);
  };

  return (
    <form onSubmit={handleSubmit} className="business-hours-form">
      <h4>🕒 Configurar horarios</h4>

      {formData.map((day, i) => (
        <div key={i} className="day-row mb-2 d-flex align-items-center">
          <label className="me-3 d-flex align-items-center">
            <input
              type="checkbox"
              checked={day.enabled}
              onChange={() => handleToggle(i)}
              className="me-2"
            />
            <strong>{day.dayOfWeek}</strong>
          </label>

          {day.enabled && (
            <div className="d-flex align-items-center gap-2">
              <input
                type="time"
                value={day.ranges[0]?.open || ""}
                onChange={(e) => handleChange(i, "open", e.target.value)}
              />
              <span>-</span>
              <input
                type="time"
                value={day.ranges[0]?.close || ""}
                onChange={(e) => handleChange(i, "close", e.target.value)}
              />
            </div>
          )}
        </div>
      ))}

      <button type="submit" className="btn btn-success mt-3">
        Guardar horarios
      </button>
    </form>
  );
};
