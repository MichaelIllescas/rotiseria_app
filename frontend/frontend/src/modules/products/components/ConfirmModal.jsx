import React from "react";
import { Button } from "../../../ui/button";

export function ConfirmModal({ title, message, onConfirm, onCancel }) {
  return (
    <div className="modal-overlay" onMouseDown={onCancel}>
      <div
        className="modal-container"
        onMouseDown={(e) => e.stopPropagation()}
        role="dialog"
        aria-modal="true"
      >
        <div className="modal-title modal-header">

        <h3 className="modal-title">{title}</h3>
        </div>
        <div className="modal-subtitle">
          <p className="modal-message">{message}</p>
        </div>

        <div className="modal-actions ">
          <div className="modal-footer ">
             <Button className="btn btn-danger" onClick={onConfirm}>
              Confirmar
            </Button>
            <Button className="btn btn-secondary" onClick={onCancel}>
              Cancelar
            </Button>
           
          </div>
        </div>
      </div>
    </div>
  );
}
