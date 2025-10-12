import React from "react";

export const BusinessHoursViewer = ({ hours = [] }) => {
  if (!hours.length) {
    return (
      <div className="text-center text-muted mt-3">
        <p>No hay horarios registrados.</p>
      </div>
    );
  }

  return (
    <div className="business-hours-viewer card p-3 shadow-sm">
      <h4 className="mb-3">🕒 Horarios de atención</h4>
      <ul className="list-unstyled m-0">
        {hours.map((h) => (
          <li
            key={h.dayOfWeek}
            className="d-flex justify-content-between align-items-center py-2 border-bottom"
          >
            <span className="fw-semibold">{h.dayOfWeek} </span>

            {h.enabled ? (
              <span className="text-success">
                {h.ranges && h.ranges.length > 0 ? (
                  h.ranges.map((r, i) => (
                    <span key={i}>
                     de {r.open} – {r.close}
                      {i < h.ranges.length - 1 && ", "}
                    </span>
                  ))
                ) : (
                  <em> Sin rango definido</em>
                )}
              </span>
            ) : (
              <span className="text-danger">Cerrado</span>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};
