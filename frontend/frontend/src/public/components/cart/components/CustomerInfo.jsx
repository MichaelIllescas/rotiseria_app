import React, { useState } from "react";
import { User, Phone, Mail, AlertCircle, CheckCircle, IdCard } from "lucide-react";
import "../../../styles/CustomerInfo.css";

export const CustomerInfo = ({ onCustomerChange, initialData = null, shippingType = "pickup" }) => {
  const [customerData, setCustomerData] = useState({
    name: initialData?.name || "",
    phone: initialData?.phone || "",
    email: initialData?.email || "",
    document: initialData?.document || "",
    observations: initialData?.observations || "",
  });

  const [errors, setErrors] = useState({});
  const [hasInteracted, setHasInteracted] = useState(false);

  const validateCustomerData = (data) => {
    const newErrors = {};

    if (!data.name.trim()) newErrors.name = "El nombre es obligatorio";
    else if (data.name.trim().length < 2)
      newErrors.name = "El nombre debe tener al menos 2 caracteres";

    if (!data.phone.trim()) newErrors.phone = "El teléfono es obligatorio";
    else if (!/^\+?[\d\s\-()]{8,15}$/.test(data.phone.replace(/\s/g, "")))
      newErrors.phone = "Ingrese un número de teléfono válido";

    if (!data.email.trim()) newErrors.email = "El email es obligatorio";
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email))
      newErrors.email = "Ingrese un email válido";

    return newErrors;
  };

  const handleInputChange = (field, value) => {
    const updatedData = { ...customerData, [field]: value };
    setCustomerData(updatedData);

    if (hasInteracted) {
      const newErrors = validateCustomerData(updatedData);
      setErrors(newErrors);
      const isValid = Object.keys(newErrors).length === 0;
      onCustomerChange?.({ ...updatedData, isValid });
    }
  };

  const handleInputBlur = () => {
    setHasInteracted(true);
    const newErrors = validateCustomerData(customerData);
    setErrors(newErrors);
    const isValid = Object.keys(newErrors).length === 0;
    onCustomerChange?.({ ...customerData, isValid });
  };

  const isFormValid =
    Object.keys(errors).length === 0 &&
    customerData.name.trim() &&
    customerData.phone.trim() &&
    customerData.email.trim();

  return (
    <div className="customer-info card">
      <div className="customer-header">
        <h3 className="customer-title">
          <User className="title-icon" /> Datos del Cliente
        </h3>
        <p className="customer-subtitle">
          {shippingType === "delivery"
            ? "Información de quien recibirá el pedido"
            : "Información de quien retirará el pedido"}
        </p>
      </div>

      <div className="customer-form">
        {/* Fila 1 */}
        <div className="form-row">
          <div className="form-group">
            <label>Nombre y Apellido *</label>
            <div className="input-wrapper">
              <User size={16} className="input-icon" />
              <input
                type="text"
                value={customerData.name}
                onChange={(e) => handleInputChange("name", e.target.value)}
                onBlur={handleInputBlur}
                placeholder="Ej: Juan Pérez"
                className={errors.name ? "error" : ""}
              />
            </div>
            {errors.name && (
              <span className="error-message">
                <AlertCircle size={12} /> {errors.name}
              </span>
            )}
          </div>

          <div className="form-group">
            <label>Teléfono *</label>
            <div className="input-wrapper">
              <Phone size={16} className="input-icon" />
              <input
                type="tel"
                value={customerData.phone}
                onChange={(e) => handleInputChange("phone", e.target.value)}
                onBlur={handleInputBlur}
                placeholder="Ej: +54 9 11 1234-5678"
                className={errors.phone ? "error" : ""}
              />
            </div>
            {errors.phone && (
              <span className="error-message">
                <AlertCircle size={12} /> {errors.phone}
              </span>
            )}
          </div>
        </div>

        {/* Fila 2 */}
        <div className="form-row">
          <div className="form-group">
            <label>Email *</label>
            <div className="input-wrapper">
              <Mail size={16} className="input-icon" />
              <input
                type="email"
                value={customerData.email}
                onChange={(e) => handleInputChange("email", e.target.value)}
                onBlur={handleInputBlur}
                placeholder="Ej: juan@email.com"
                className={errors.email ? "error" : ""}
              />
            </div>
            {errors.email && (
              <span className="error-message">
                <AlertCircle size={12} /> {errors.email}
              </span>
            )}
          </div>

          <div className="form-group">
            <label>DNI (opcional)</label>
            <div className="input-wrapper">
              <IdCard size={16} className="input-icon" />
              <input
                type="text"
                value={customerData.document}
                onChange={(e) => handleInputChange("document", e.target.value)}
                placeholder="Ej: 12345678"
              />
            </div>
          </div>
        </div>

        {/* Observaciones */}
        <div className="form-group">
          <label>Observaciones (opcional)</label>
          <textarea className="textarea"
            value={customerData.observations}
            onChange={(e) => handleInputChange("observations", e.target.value)}
            placeholder="Comentarios adicionales para el pedido..."
            rows={3}
          />
        </div>

        {/* Estado del formulario */}
        {hasInteracted && (
          <div className={`form-status ${isFormValid ? "valid" : "invalid"}`}>
            {isFormValid ? (
              <>
                <CheckCircle size={16} />
                <span>Información completa</span>
              </>
            ) : (
              <>
                <AlertCircle size={16} />
                <span>Complete los campos obligatorios</span>
              </>
            )}
          </div>
        )}
      </div>
    </div>
  );
};
