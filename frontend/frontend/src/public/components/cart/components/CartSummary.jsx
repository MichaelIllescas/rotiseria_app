import React from 'react';
import { useNavigate } from 'react-router-dom';
import { ShoppingBag, ArrowLeft } from 'lucide-react';
import '../../../styles/CartSummary.css';

export const CartSummary = ({
  cart = [],
  total,
  shippingCost = 1500, // valor fijo o pasado por prop
  onClear,
  onContinueToPayment,
  isCheckoutReady = false
}) => {
  const navigate = useNavigate();
  const itemCount = cart.reduce((sum, item) => sum + item.quantity, 0);
  const subtotal = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
  const totalWithShipping = subtotal + shippingCost;

  const handleContinueShopping = () => {
    navigate('/');
  };

  return (
    <div className="cart-summary">
      <div className="summary-header">
        <h3>Resumen del Pedido</h3>
        <p className="summary-subtitle">
          {itemCount} {itemCount === 1 ? 'producto' : 'productos'} en tu carrito
        </p>
      </div>

      <div className="summary-items">
        {cart.map((item) => (
          <div key={item.id} className="summary-item">
            <div className="summary-item-info">
              <span className="summary-item-name">{item.name}</span>
              <span className="summary-item-quantity">x{item.quantity}</span>
            </div>
            <span className="summary-item-price">
              ${(item.price * item.quantity).toLocaleString()}
            </span>
          </div>
        ))}
      </div>

      <div className="summary-total">
        <div className="total-row">
          <span className="total-label">Subtotal:</span>
          <span className="total-amount">${subtotal.toLocaleString()}</span>
        </div>

        <div className="total-row">
          <span className="total-label">Envío:</span>
          <span className="total-amount">${shippingCost.toLocaleString()}</span>
        </div>

        <hr />

        <div className="total-row total-final">
          <span className="total-label">Total:</span>
          <span className="total-amount">
            ${totalWithShipping.toLocaleString()}
          </span>
        </div>
      </div>

      <div className="summary-actions">
        <div className="action-row">
          <button
            onClick={handleContinueShopping}
            className="btn btn-outline continue-shopping-button"
          >
            <ArrowLeft size={18} />
            Seguir Comprando
          </button>

          <button
            onClick={onClear}
            className="btn btn-outline-secondary clear-button"
          >
            Vaciar Carrito
          </button>
        </div>

        <button
          onClick={isCheckoutReady ? onContinueToPayment : undefined}
          className={`btn btn-primary continue-button ${!isCheckoutReady ? 'disabled' : ''}`}
          disabled={!isCheckoutReady}
        >
          <ShoppingBag size={18} />
          Continuar al Pago
        </button>
      </div>
    </div>
  );
};
