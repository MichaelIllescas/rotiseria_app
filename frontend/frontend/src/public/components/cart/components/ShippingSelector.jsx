import React, { useState, useEffect } from "react";
import {
  Store,
  Truck,
  MapPin,
  Clock,
  AlertCircle,
  CheckCircle,
} from "lucide-react";
import "../../../styles/ShippingSelector.css";

export const ShippingSelector = ({
  onShippingChange,
  selectedShipping = "pickup",
  business = {},
  initialAddress = null,
  shippingCost = 0,
}) => {
  const [selected, setSelected] = useState(selectedShipping);
  const [address, setAddress] = useState({
    street: initialAddress?.street || "",
    number: initialAddress?.number || "",
    locality: initialAddress?.locality || "",
    reference: initialAddress?.reference || "",
  });
  const [errors, setErrors] = useState({});
  const [hasInteracted, setHasInteracted] = useState(false);
  const [infoCompleted, setInfoCompleted] = useState(false);

  const shippingOptions = [
    {
      id: "pickup",
      title: "Retiro en Sucursal",
      description: "Retirá tu pedido en nuestro local",
      icon: Store,
      price: 0,
      estimatedTime: "15-30 min",
      details: business.address || "Dirección de la sucursal",
      badge: "Gratis",
      badgeColor: "success",
    },
    {
      id: "delivery",
      title: "Envío a Domicilio",
      description: "Te llevamos tu pedido donde estés",
      icon: Truck,
      price: shippingCost,
      estimatedTime: "30-45 min",
      details: "Cobertura en zona de influencia",
      badge: "Rápido",
      badgeColor: "primary",
    },
  ];

  // 🔹 Validación básica de dirección
  const validateAddressData = (data) =>
    data.street.trim() && data.number.trim() && data.locality.trim();

  const validateAddress = () => {
    const newErrors = {};
    if (!address.street.trim()) newErrors.street = "La calle es obligatoria";
    if (!address.number.trim()) newErrors.number = "El número es obligatorio";
    if (!address.locality.trim())
      newErrors.locality = "La localidad es obligatoria";
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // 🔹 Notificar al padre en el montaje inicial
  useEffect(() => {
    const selectedOption = shippingOptions.find((opt) => opt.id === selected);
    const isValid = selected === "pickup" ? true : validateAddressData(address);

    onShippingChange &&
      onShippingChange({
        ...selectedOption,
        type: selected,
        cost: selectedOption?.price || 0,
        address: selected === "pickup" ? null : { ...address },
        isValid,
      });

    setInfoCompleted(isValid);
  }, []); // Solo al montar

  // 🔹 Actualizar al cambiar tipo o campos de dirección
  // 🔹 Forzar actualización del padre cuando cambia address o tipo
  useEffect(() => {
    const selectedOption = shippingOptions.find((opt) => opt.id === selected);
    const isValid = selected === "pickup" ? true : validateAddressData(address);

    onShippingChange({
      ...selectedOption,
      type: selected,
      cost: selectedOption?.price || 0,
      address: selected === "pickup" ? null : { ...address }, // 👈 fuerza nueva referencia
      isValid: !!isValid, // 👈 convierte a booleano puro
    });

    setInfoCompleted(isValid);
  }, [address.street, address.number, address.locality, selected]);

  // 🔹 Al seleccionar método
  const handleSelect = (optionId) => {
    setSelected(optionId);
    const selectedOption = shippingOptions.find((opt) => opt.id === optionId);

    if (optionId === "pickup") {
      setErrors({});
      setHasInteracted(false);
      setInfoCompleted(true);

      onShippingChange &&
        onShippingChange({
          ...selectedOption,
          type: optionId,
          cost: selectedOption?.price || 0,
          address: null,
          isValid: true,
        });
    } else {
      const isValid = validateAddressData(address);
      setInfoCompleted(isValid);
      onShippingChange &&
        onShippingChange({
          ...selectedOption,
          type: optionId,
          cost: selectedOption?.price || 0,
          address: { ...address },
          isValid,
        });
    }
  };

  // 🔹 Al cambiar un campo de dirección
  const handleAddressChange = (field, value) => {
    const newAddress = { ...address, [field]: value };
    setAddress(newAddress);
    setHasInteracted(true);

    if (errors[field]) {
      setErrors((prev) => ({ ...prev, [field]: "" }));
    }

    if (selected === "delivery") {
      const isValid = validateAddressData(newAddress);
      const selectedOption = shippingOptions.find(
        (opt) => opt.id === "delivery"
      );
      setInfoCompleted(isValid);

      onShippingChange &&
        onShippingChange({
          ...selectedOption,
          type: "delivery",
          cost: selectedOption?.price || 0,
          address: { ...newAddress },
          isValid,
        });
    }
  };

  const handleAddressBlur = () => {
    if (selected === "delivery") {
      setHasInteracted(true);
      validateAddress();
      const isValid = validateAddressData(address);
      const selectedOption = shippingOptions.find(
        (opt) => opt.id === "delivery"
      );
      setInfoCompleted(isValid);

      onShippingChange &&
        onShippingChange({
          ...selectedOption,
          type: "delivery",
          cost: selectedOption?.price || 0,
          address: { ...address },
          isValid,
        });
    }
  };

  return (
    <div className="shipping-selector">
      <div className="shipping-header">
        <h3 className="shipping-title">
          <Truck className="title-icon" />
          Forma de Entrega
        </h3>
        <p className="shipping-subtitle">Elegí cómo querés recibir tu pedido</p>
      </div>

      {/* === Opciones === */}
      <div className="shipping-options">
        {shippingOptions.map((option) => {
          const IconComponent = option.icon;
          const isSelected = selected === option.id;

          return (
            <div
              key={option.id}
              className={`shipping-option ${isSelected ? "selected" : ""}`}
              onClick={() => handleSelect(option.id)}
            >
              <div className="option-header">
                <div className="option-icon">
                  <IconComponent size={24} />
                </div>
                <div className="option-badge">
                  <span className={`badge ${option.badgeColor}`}>
                    {option.badge}
                  </span>
                </div>
              </div>

              <div className="option-content">
                <h4 className="option-title">{option.title}</h4>
                <p className="option-description">{option.description}</p>
                <div className="option-details">
                  <div className="detail-item">
                    <MapPin size={14} />
                    <span>{option.details}</span>
                  </div>
                  <div className="detail-item">
                    <Clock size={14} />
                    <span>{option.estimatedTime}</span>
                  </div>
                </div>
                <div className="option-price">
                  {option.price === 0 ? (
                    <span className="free-price">Gratis</span>
                  ) : (
                    <span className="paid-price">${option.price}</span>
                  )}
                </div>
              </div>

              <div className="option-radio">
                <div className={`radio-button ${isSelected ? "checked" : ""}`}>
                  {isSelected && <div className="radio-dot"></div>}
                </div>
              </div>
            </div>
          );
        })}
      </div>

      {/* === Info Retiro === */}
      {selected === "pickup" && business.address && (
        <div className="pickup-info">
          <div className="info-card">
            <Store size={16} />
            <div className="info-content">
              <h5>Dirección de Retiro</h5>
              <p>{business.address}</p>
              {business.phone && <p className="phone">📞 {business.phone}</p>}
              <div className="pickup-note">
                <AlertCircle size={14} />
                <span>No necesitás proporcionar dirección para el retiro</span>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* === Info Delivery === */}
      {selected === "delivery" && (
        <div className="delivery-info">
          <div className="info-card">
            <Truck size={16} />
            <div className="info-content">
              <h5>Dirección de Entrega</h5>
              <p className="delivery-description">
                Completá todos los campos para confirmar la entrega a domicilio
              </p>

              <form className="address-form">
                <div className="form-row">
                  <div className="form-group">
                    <label htmlFor="street">Calle *</label>
                    <input
                      type="text"
                      id="street"
                      value={address.street}
                      onChange={(e) =>
                        handleAddressChange("street", e.target.value)
                      }
                      onBlur={handleAddressBlur}
                      placeholder="Ej: Av. Corrientes"
                      className={errors.street ? "error" : ""}
                    />
                    {errors.street && (
                      <span className="error-message">
                        <AlertCircle size={12} />
                        {errors.street}
                      </span>
                    )}
                  </div>

                  <div className="form-group">
                    <label htmlFor="number">Número *</label>
                    <input
                      type="text"
                      id="number"
                      value={address.number}
                      onChange={(e) =>
                        handleAddressChange("number", e.target.value)
                      }
                      onBlur={handleAddressBlur}
                      placeholder="Ej: 1234"
                      className={errors.number ? "error" : ""}
                    />
                    {errors.number && (
                      <span className="error-message">
                        <AlertCircle size={12} />
                        {errors.number}
                      </span>
                    )}
                  </div>
                </div>

                <div className="form-group">
                  <label htmlFor="locality">Localidad *</label>
                  <input
                    type="text"
                    id="locality"
                    value={address.locality}
                    onChange={(e) =>
                      handleAddressChange("locality", e.target.value)
                    }
                    onBlur={handleAddressBlur}
                    placeholder="Ej: Buenos Aires"
                    className={errors.locality ? "error" : ""}
                  />
                  {errors.locality && (
                    <span className="error-message">
                      <AlertCircle size={12} />
                      {errors.locality}
                    </span>
                  )}
                </div>

                <div className="form-group">
                  <label htmlFor="reference">Referencia (opcional)</label>
                  <input
                    type="text"
                    id="reference"
                    value={address.reference}
                    onChange={(e) =>
                      handleAddressChange("reference", e.target.value)
                    }
                    placeholder="Ej: Depto 4B, Entre Juan y Pedro"
                  />
                </div>
              </form>

              <div className="delivery-note">
                <p className="small-text">
                  El tiempo puede variar según la ubicación y condiciones del
                  tráfico
                </p>

                {hasInteracted && Object.keys(errors).length > 0 && (
                  <div className="validation-warning">
                    <AlertCircle size={14} />
                    <span>
                      Completá todos los campos obligatorios para continuar
                    </span>
                  </div>
                )}

                {infoCompleted && (
                  <div
                    className="validation-success"
                    style={{
                      backgroundColor: "#ecfdf5",
                      color: "#065f46",
                      fontSize: "0.875rem",
                      padding: "0.75rem",
                      borderRadius: "6px",
                      borderLeft: "3px solid #10b981",
                      marginTop: "0.5rem",
                      display: "flex",
                      alignItems: "center",
                      gap: "0.5rem",
                    }}
                  >
                    <CheckCircle size={14} />
                    <span>Información de entrega completa</span>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
