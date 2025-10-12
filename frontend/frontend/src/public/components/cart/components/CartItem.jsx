import React from "react";
import { Plus, Minus, Trash2 } from "lucide-react";
import "../../../styles/CartItem.css";

export const CartItem = ({ item, onRemove, onUpdateQuantity }) => {
  const handleQuantityChange = (change) => {
    const newQuantity = item.quantity + change;
    if (newQuantity > 0) {
      onUpdateQuantity(item.id, newQuantity);
    } else {
      onRemove(item.id);
    }
  };

  const totalPrice = item.price * item.quantity;

  return (
    <div className="cart-item">
      {/* Sección izquierda: imagen + info */}
      <div className="item-left">
        <img
          src={
            item.imageUrl?.startsWith("http")
              ? item.imageUrl
              : `http://localhost:8080${item.imageUrl}`
          }
          alt={item.name}
        />
        <div className="item-info">
          <h4>{item.name}</h4>
          <p>${item.price} c/u</p>
        </div>
      </div>

      {/* Sección derecha: cantidad + total + quitar */}
      <div className="item-right">
        <div className="quantity-controls">
          <button
            className="quantity-btn"
            onClick={() => handleQuantityChange(-1)}
            disabled={item.quantity <= 1}
          >
            <Minus size={16} />
          </button>
          <span className="quantity-display">{item.quantity}</span>
          <button
            className="quantity-btn"
            onClick={() => handleQuantityChange(1)}
          >
            <Plus size={16} />
          </button>
        </div>

        <div className="item-total">${totalPrice.toLocaleString()}</div>

        <button
          className="remove-btn"
          onClick={() => onRemove(item.id)}
        >
          <Trash2 size={16} />
          Quitar
        </button>
      </div>
    </div>
  );
};
