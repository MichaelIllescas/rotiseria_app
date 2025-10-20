import React, { useState } from 'react';
import { CreditCard, HandCoins, CheckCircle, Clock, ExternalLink } from 'lucide-react';
import '../../../styles/PaymentSelector.css';

export const PaymentSelector = ({ 
  onPaymentChange, 
  selectedPayment = 'cash',
  orderTotal = 0,
  onMercadoPagoRedirect
}) => {
  const [selected, setSelected] = useState(selectedPayment);

  const paymentOptions = [
    {
      id: 'cash',
      title: 'Pago en Efectivo',
      subtitle: 'Contraentrega',
      description: 'Pagá cuando recibas tu pedido',
      icon: HandCoins,
      badge: 'Fácil',
      badgeColor: 'success',
      details: 'Pago al momento de la entrega o retiro',
      advantages: ['Sin comisiones', 'Pago directo', 'Sin registración'],
      note: 'Llevá el monto exacto para facilitar el proceso'
    },
    {
      id: 'mercadopago',
      title: 'Mercado Pago',
      subtitle: 'Online',
      description: 'Pagá de forma segura con tarjeta de crédito o débito',
      icon: CreditCard,
      badge: 'Seguro',
      badgeColor: 'primary',
      details: 'Tarjetas de crédito, débito y transferencia',
      advantages: ['Pago inmediato', 'Múltiples medios', 'Protección del comprador'],
      note: 'Serás redirigido a Mercado Pago para completar el pago'
    }
  ];

  const handleSelect = (optionId) => {
    setSelected(optionId);
    const selectedOption = paymentOptions.find(opt => opt.id === optionId);
    
    const paymentData = {
      ...selectedOption,
      isValid: true // Ambas opciones son válidas al seleccionarlas
    };
    
    onPaymentChange && onPaymentChange(paymentData);
  };

  const handleMercadoPagoClick = () => {
    if (selected === 'mercadopago') {
      onMercadoPagoRedirect && onMercadoPagoRedirect({
        total: orderTotal,
        paymentMethod: 'mercadopago'
      });
    }
  };

  return (
    <div className="payment-selector">
      <div className="payment-header">
        <h3 className="payment-title">
          <CreditCard className="title-icon" />
          Método de Pago
        </h3>
        <p className="payment-subtitle">
          Elegí cómo querés pagar tu pedido
        </p>
        <div className="order-total">
          <span>Total a pagar: <strong>${orderTotal}</strong></span>
        </div>
      </div>

      <div className="payment-options">
        {paymentOptions.map((option) => {
          const IconComponent = option.icon;
          const isSelected = selected === option.id;
          
          return (
            <div
              key={option.id}
              className={`payment-option ${isSelected ? 'selected' : ''}`}
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
                  <p className="details-text">{option.details}</p>
                </div>

                <div className="option-advantages">
                  <ul className="advantages-list">
                    {option.advantages.map((advantage, index) => (
                      <li key={index}>
                        <CheckCircle size={12} />
                        <span>{advantage}</span>
                      </li>
                    ))}
                  </ul>
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



      {selected === 'mercadopago' && (
        <div className="payment-info mercadopago-info">
          <div className="info-card">
            <CreditCard size={16} />
            <div className="info-content">
              <h5>Mercado Pago</h5>
              <p>Serás redirigido a Mercado Pago para completar el pago de forma segura.</p>
              
              <div className="mp-process">
                <h6>Proceso de pago:</h6>
                <ol>
                  <li>Clic en "Pagar con Mercado Pago"</li>
                  <li>Elegís tu método de pago preferido</li>
                  <li>Confirmás el pago</li>
                  <li>Recibís la confirmación del pedido</li>
                </ol>
              </div>

              <div className="mp-note">
                <p>🔒 <strong>Seguridad:</strong> {paymentOptions[1].note}</p>
              </div>

              <button 
                className="mp-redirect-btn"
                onClick={handleMercadoPagoClick}
                disabled={!orderTotal}
              >
                <CreditCard size={16} />
                <span>Pagar con Mercado Pago</span>
                <ExternalLink size={14} />
              </button>
            </div>
          </div>
        </div>
      )}

   
    </div>
  );
};