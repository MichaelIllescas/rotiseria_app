import React, { useState } from 'react';
import { CreditCard, HandCoins, CheckCircle, ArrowLeft } from 'lucide-react';
import '../../../styles/PaymentMethod.css';

export const PaymentMethod = ({ 
  total,            // subtotal de productos
  onPaymentSelect,
  onBack,
  shippingData = null
}) => {
  const [selectedMethod, setSelectedMethod] = useState(null);

  // Extraer costo de envío de shippingData (seguro)
  const shippingCost = Number(shippingData?.cost || shippingData?.price || 0);
  const totalToPay = total + shippingCost; // 👈 total final real

  const paymentMethods = [
    {
      id: 'mercadopago',
      title: 'Mercado Pago',
      subtitle: 'Pago online seguro',
      description: 'Pagá con tarjeta de crédito, débito o transferencia',
      icon: CreditCard,
      badge: 'Inmediato',
      badgeColor: 'primary',
      advantages: [
        'Pago inmediato',
        'Múltiples medios de pago',
        'Protección del comprador',
        'Sin necesidad de efectivo'
      ]
    },
    {
      id: 'cash',
      title: 'Pago en Efectivo',
      subtitle: shippingData?.type === 'delivery' ? 'Al recibir' : 'Al retirar',
      description: shippingData?.type === 'delivery' 
        ? 'Pagá cuando recibas tu pedido en tu domicilio'
        : 'Pagá cuando retires tu pedido en el local',
      icon: HandCoins,
      badge: 'Fácil',
      badgeColor: 'success',
      advantages: [
        'Sin comisiones',
        'Pago directo',
        'Sin registración necesaria',
        'Ideal para montos exactos'
      ]
    }
  ];

  const handleMethodSelect = (methodId) => {
    setSelectedMethod(methodId);
  };

  const handleContinue = () => {
    if (selectedMethod) {
      const method = paymentMethods.find((m) => m.id === selectedMethod);
      onPaymentSelect({
        method: selectedMethod,
        ...method
      });
    }
  };

  return (
    <div className="cart-container">
      {/* === Header === */}
      <div className="cart-header cart-header-banner">
        <button onClick={onBack} className="back-button-banner">
          <ArrowLeft size={20} />
          Volver al carrito
        </button>

        <h1>💳 ¡Elegí tu forma de pago!</h1>
        <p className="cart-subtitle">
          Seleccioná el método que más te convenga para completar tu pedido
        </p>
        <div className="step-indicator">
          <span className="step-number">2</span>
          <span className="step-text">de 3</span>
        </div>
      </div>

      {/* === Contenido === */}
      <div className="cart-content">
        <section className="payment-method-section">
          <div className="method-header">
            <div className="order-summary">
              {/* 🧾 Totales */}
              <div className="summary-row">
                <span>💲Subtotal:</span>
                <span>${total.toLocaleString()}</span>
              </div>

              {shippingData && (
                <div className="summary-row shipping-info">
                  <span>
                    {shippingData.type === 'delivery' || shippingData.id === 'delivery'
                      ? '📦 Envío a domicilio'
                      : '🏪 Retiro en local'}
                  </span>
                  <span>
                    {shippingCost > 0 ? `$${shippingCost}` : 'Gratis'}
                  </span>
                </div>
              )}

              <hr />

              <div className="summary-row total-to-pay">
                <span className="total-label">Total a pagar:</span>
                <span className="total-amount">
                  ${totalToPay.toLocaleString()}
                </span>
              </div>
            </div>
          </div>

          {/* Opciones de pago */}
          <div className="payment-options">
            {paymentMethods.map((method) => {
              const IconComponent = method.icon;
              const isSelected = selectedMethod === method.id;

              return (
                <div
                  key={method.id}
                  className={`payment-option ${isSelected ? 'selected' : ''}`}
                  onClick={() => handleMethodSelect(method.id)}
                >
                  <div className="option-layout">
                    <div className="option-header">
                      <div className="option-icon">
                        <IconComponent size={24} />
                      </div>
                      <div className="option-badge">
                        <span className={`badge ${method.badgeColor}`}>
                          {method.badge}
                        </span>
                      </div>
                    </div>

                    <div className="option-content">
                      <div className="option-main">
                        <h3 className="option-title">{method.title}</h3>
                        <p className="option-subtitle">{method.subtitle}</p>
                        <p className="option-description">{method.description}</p>
                      </div>

                      <div className="option-advantages">
                        <ul className="advantages-list">
                          {method.advantages.map((advantage, index) => (
                            <li key={index}>
                              <CheckCircle size={14} />
                              <span>{advantage}</span>
                            </li>
                          ))}
                        </ul>
                      </div>
                    </div>

                    <div className="option-radio-payment">
                      <div
                        className={`radio-button ${isSelected ? 'checked' : ''}`}
                      >
                        {isSelected && <div className="radio-dot"></div>}
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>

          {/* Botón continuar */}
          <div className="payment-actions">
            <button
              onClick={handleContinue}
              className={`continue-button ${!selectedMethod ? 'disabled' : ''}`}
              disabled={!selectedMethod}
            >
              {selectedMethod === 'mercadopago'
                ? 'Ir a Mercado Pago'
                : 'Confirmar Pedido'}
            </button>
          </div>
        </section>
      </div>
    </div>
  );
};
