import React, { useState } from 'react';
import { Store, Truck, MapPin, Clock, AlertCircle } from 'lucide-react';
import '../../../styles/ShippingSelector.css';

export const ShippingSelector = ({ 
  onShippingChange, 
  selectedShipping = 'pickup',
  business = {},
  initialAddress = null
}) => {
  const [selected, setSelected] = useState(selectedShipping);
  const [address, setAddress] = useState({
    street: initialAddress?.street || '',
    number: initialAddress?.number || '',
    locality: initialAddress?.locality || '',
    reference: initialAddress?.reference || ''
  });
  const [errors, setErrors] = useState({});
  const [hasInteracted, setHasInteracted] = useState(false); // Nuevo estado

  const shippingOptions = [
    {
      id: 'pickup',
      title: 'Retiro en Sucursal',
      subtitle: 'Gratis',
      description: 'Retirá tu pedido en nuestro local',
      icon: Store,
      price: 0,
      estimatedTime: '15-30 min',
      details: business.address || 'Dirección de la sucursal',
      badge: 'Gratis',
      badgeColor: 'success'
    },
    {
      id: 'delivery',
      title: 'Envío a Domicilio',
      subtitle: 'Delivery',
      description: 'Te llevamos tu pedido donde estés',
      icon: Truck,
      price: 500,
      estimatedTime: '30-45 min',
      details: 'Cobertura en zona de influencia',
      badge: 'Rápido',
      badgeColor: 'primary'
    }
  ];

  const validateAddress = () => {
    const newErrors = {};
    
    if (!address.street.trim()) {
      newErrors.street = 'La calle es obligatoria';
    }
    
    if (!address.number.trim()) {
      newErrors.number = 'El número es obligatorio';
    }
    
    if (!address.locality.trim()) {
      newErrors.locality = 'La localidad es obligatoria';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSelect = (optionId) => {
    setSelected(optionId);
    const selectedOption = shippingOptions.find(opt => opt.id === optionId);
    
    // Si es pickup, limpiar errores de dirección y resetear interacción
    if (optionId === 'pickup') {
      setErrors({});
      setHasInteracted(false);
    }
    
    // Si cambia a delivery, resetear la interacción para no validar inmediatamente
    if (optionId === 'delivery') {
      setHasInteracted(false);
      setErrors({}); // Limpiar errores previos
    }
    
    // Preparar los datos para enviar al padre
    const shippingData = {
      ...selectedOption,
      address: optionId === 'delivery' ? address : null,
      isValid: optionId === 'pickup' ? true : (hasInteracted ? validateAddressData(address) : false)
    };
    
    onShippingChange && onShippingChange(shippingData);
  };

  const handleAddressChange = (field, value) => {
    const newAddress = { ...address, [field]: value };
    setAddress(newAddress);
    
    // Marcar que el usuario ha interactuado
    if (!hasInteracted) {
      setHasInteracted(true);
    }
    
    // Limpiar error del campo que se está editando solo si ya había interactuado
    if (errors[field] && hasInteracted) {
      setErrors(prev => ({ ...prev, [field]: '' }));
    }
    
    // Si ya está seleccionado delivery y ha interactuado, validar y notificar cambios
    if (selected === 'delivery' && hasInteracted) {
      const selectedOption = shippingOptions.find(opt => opt.id === 'delivery');
      const isValid = validateAddressData(newAddress);
      
      const shippingData = {
        ...selectedOption,
        address: newAddress,
        isValid
      };
      
      onShippingChange && onShippingChange(shippingData);
    }
  };

  const validateAddressData = (addressData) => {
    return addressData.street.trim() && 
           addressData.number.trim() && 
           addressData.locality.trim();
  };

  const handleAddressBlur = () => {
    if (selected === 'delivery') {
      setHasInteracted(true); // Marcar interacción al hacer blur
      validateAddress();
      
      // Notificar al padre sobre la validación
      const selectedOption = shippingOptions.find(opt => opt.id === 'delivery');
      const isValid = validateAddressData(address);
      
      const shippingData = {
        ...selectedOption,
        address: address,
        isValid
      };
      
      onShippingChange && onShippingChange(shippingData);
    }
  };

  return (
    <div className="shipping-selector">
      <div className="shipping-header">
        <h3 className="shipping-title">
          <Truck className="title-icon" />
          Forma de Entrega
        </h3>
        <p className="shipping-subtitle">
          Elegí cómo querés recibir tu pedido
        </p>
      </div>

      <div className="shipping-options">
        {shippingOptions.map((option) => {
          const IconComponent = option.icon;
          const isSelected = selected === option.id;
          
          return (
            <div
              key={option.id}
              className={`shipping-option ${isSelected ? 'selected' : ''}`}
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
                <div className="option-main">
                  <h4 className="option-title">{option.title}</h4>
                  <p className="option-description">{option.description}</p>
                </div>

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
                <div className={`radio-button ${isSelected ? 'checked' : ''}`}>
                  {isSelected && <div className="radio-dot"></div>}
                </div>
              </div>
            </div>
          );
        })}
      </div>

      {/* Información de Pickup */}
      {selected === 'pickup' && business.address && (
        <div className="pickup-info">
          <div className="info-card">
            <Store size={16} />
            <div className="info-content">
              <h5>Dirección de Retiro</h5>
              <p>{business.address}</p>
              {business.phone && (
                <p className="phone">📞 {business.phone}</p>
              )}
              <div className="pickup-note">
                <AlertCircle size={14} />
                <span>No necesitás proporcionar dirección para el retiro</span>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Formulario de Dirección para Delivery */}
      {selected === 'delivery' && (
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
                      onChange={(e) => handleAddressChange('street', e.target.value)}
                      onBlur={handleAddressBlur}
                      placeholder="Ej: Av. Corrientes"
                      className={errors.street ? 'error' : ''}
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
                      onChange={(e) => handleAddressChange('number', e.target.value)}
                      onBlur={handleAddressBlur}
                      placeholder="Ej: 1234"
                      className={errors.number ? 'error' : ''}
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
                    onChange={(e) => handleAddressChange('locality', e.target.value)}
                    onBlur={handleAddressBlur}
                    placeholder="Ej: Buenos Aires"
                    className={errors.locality ? 'error' : ''}
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
                    onChange={(e) => handleAddressChange('reference', e.target.value)}
                    placeholder="Ej: Depto 4B, Entre Juan y Pedro"
                  />
                </div>
              </form>

              <div className="delivery-note">
                <p className="small-text">
                  El tiempo puede variar según la ubicación y condiciones del tráfico
                </p>
                {hasInteracted && Object.keys(errors).length > 0 && (
                  <div className="validation-warning">
                    <AlertCircle size={14} />
                    <span>Completá todos los campos obligatorios para continuar</span>
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