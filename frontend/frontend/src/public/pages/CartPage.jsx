import React from "react";
import { Link } from "react-router-dom";
import { ShoppingCart, ArrowLeft } from "lucide-react";
import { useCart } from "../../context/CartContext";
import { CartItem } from "../components/cart/components/CartItem";
import { CartSummary } from "../components/cart/components/CartSummary";
import "../styles/CartPage.css";

export const CartPage = () => {
  const { cart, total, removeFromCart, updateQuantity, clearCart } = useCart();

  if (cart.length === 0) {
    return (
      <div className="cart-page">
        <div className="cart-container">
          <div className="empty-cart">
            <div className="empty-cart-icon">🛒</div>
            <h3>Tu carrito está vacío</h3>
            <p>
              Parece que aún no has agregado ningún producto delicioso a tu
              carrito. ¡Explora nuestro menú y encuentra tus platos favoritos!
            </p>
            <Link to="/" className="continue-shopping-btn">
              <ArrowLeft size={20} style={{ marginRight: "0.5rem" }} />
              Explorar Menú
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="cart-page">
      <div className="cart-container">
        {/* Header */}
        <div className="cart-header">
          <h1>
            <ShoppingCart
              size={40}
              style={{ marginRight: "1rem", verticalAlign: "middle" }}
            />
            Mi Carrito
          </h1>
          <p className="cart-subtitle">
            Revisa tus productos seleccionados y procede con tu pedido
          </p>
          <div className="div-continue-shopping-btn">
            <Link to="/" className="continue-shopping-btn">
              ← Seguir Comprando
            </Link>
          </div>
        </div>

        {/* Content */}
        <div className="cart-content">
          {/* Items Section */}
          <div className="cart-items-section">
            <div className="cart-items-header">
              <ShoppingCart size={20} style={{ marginRight: "0.5rem" }} />
              Productos en tu carrito ({cart.length}{" "}
              {cart.length === 1 ? "producto" : "productos"})
            </div>
            <div className="cart-items-list">
              {cart.map((item) => (
                <CartItem
                  key={item.id}
                  item={item}
                  onRemove={removeFromCart}
                  onUpdateQuantity={updateQuantity}
                />
              ))}
            </div>
          </div>

          {/* Summary Section */}
          <div className="cart-summary-section">
            <CartSummary total={total} onClear={clearCart} />
          </div>
        </div>
      </div>
    </div>
  );
};
