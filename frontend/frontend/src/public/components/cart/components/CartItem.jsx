import React from "react";
import { Plus, Minus, Trash2 } from "lucide-react";
import '../../../styles/CartItem.css';

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
    <div className="cart-item flex justify-between items-center border-b py-3">
      <div className="flex items-center gap-3">
        <img src={
                  item.imageUrl?.startsWith("http")
                    ? item.imageUrl
                    : `http://localhost:8080${item.imageUrl}`
                } alt={item.name} className="w-16 h-16 object-cover" />
        <div>
          <h4>{item.name}</h4>
          <p>${item.price} × {item.quantity}</p>
        </div>
      </div>
      <div className="item-controls flex items-center gap-2">
        <div className="quantity-controls flex items-center border rounded">
          <button
            className="quantity-btn px-3 py-1"
            onClick={() => handleQuantityChange(-1)}
            disabled={item.quantity <= 1}
          >
            <Minus size={16} />
          </button>
          <span className="quantity-display px-3">{item.quantity}</span>
          <button
            className="quantity-btn px-3 py-1"
            onClick={() => handleQuantityChange(1)}
          >
            <Plus size={16} />
          </button>
        </div>
        <div className="item-total font-semibold">
          ${totalPrice.toLocaleString()}
        </div>
        <button 
          className="remove-btn flex items-center gap-1 px-3 py-1 border rounded text-red-600"
          onClick={() => onRemove(item.id)}
        >
          <Trash2 size={16} />
          Quitar
        </button>
      </div>
    </div>
  );
};
