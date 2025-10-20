import React, { useState, useEffect } from "react";
import { X, ShoppingCart, Plus, Minus } from "lucide-react";
import "../styles/ProductDetailModal.css";

export const ProductDetailModal = ({ product, isOpen, onClose, onAddToCart, categories }) => {
  const [quantity, setQuantity] = useState(1);
  const [fitMode, setFitMode] = useState("cover");


  // Resetear cantidad cuando cambie el producto
  useEffect(() => {
    if (product) {
      setQuantity(1);
    }
  }, [product]);

  // Cerrar modal con ESC
  useEffect(() => {
    const handleEsc = (e) => {
      if (e.keyCode === 27) onClose();
    };
    if (isOpen) {
      document.addEventListener("keydown", handleEsc);
      document.body.style.overflow = "hidden";
    }
    return () => {
      document.removeEventListener("keydown", handleEsc);
      document.body.style.overflow = "unset";
    };
  }, [isOpen, onClose]);

  if (!isOpen || !product) return null;

  const { name, description, price, imageUrl, dailyStock, categoryId } = product;

  // Encontrar categoría
  const category = categories?.find(cat => cat.id === categoryId);
  const categoryName = category?.name || "Sin categoría";

  const stockLabel = dailyStock > 0 ? `${dailyStock} disponibles` : "Sin stock";
  const isOutOfStock = dailyStock === 0;
  const maxQuantity = Math.min(dailyStock, 10); // Máximo 10 unidades

  const handleImageLoad = (e) => {
    const img = e.target;
    const ratio = img.naturalWidth / img.naturalHeight;
    if (ratio > 1.3) setFitMode("cover");
    else setFitMode("contain");
  };

  const handleQuantityChange = (change) => {
    const newQuantity = quantity + change;
    if (newQuantity >= 1 && newQuantity <= maxQuantity) {
      setQuantity(newQuantity);
    }
  };

  const handleAddToCart = () => {
    onAddToCart(product, quantity);
    onClose();
  };

  const totalPrice = price * quantity;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="product-detail-modal" onClick={(e) => e.stopPropagation()}>
        {/* Header del Modal */}
        <div className="modal-header">
          <button className="close-btn" onClick={onClose}>
            <X size={24} />
          </button>
        </div>

        {/* Contenido del Modal */}
        <div className="modal-content">
          {/* Imagen del Producto */}
          <div className="product-image-section">
            <div className={`product-image-container ${isOutOfStock ? "out-of-stock" : ""}`}>
              <img
                src={
                  imageUrl?.startsWith("http")
                    ? imageUrl
                    : `http://localhost:8080${imageUrl}`
                }
                alt={name}
                onLoad={handleImageLoad}
                style={{ objectFit: fitMode }}
              />
              {isOutOfStock && (
                <div className="out-of-stock-overlay">
                  <span>Sin Stock</span>
                </div>
              )}
            </div>
          </div>

          {/* Información del Producto */}
          <div className="product-info-section">
            <div className="product-category">
              <span className="category-badge">{categoryName}</span>
            </div>

            <h2 className="product-title">{name}</h2>
            
            <div className="product-description">
              <p>{description}</p>
            </div>

            <div className="product-pricing">
              <div className="price-section">
                <span className="current-price">${price.toLocaleString()}</span>
                <span className="price-unit">por unidad</span>
              </div>
              
              <div className={`stock-info ${isOutOfStock ? "no-stock" : "in-stock"}`}>
                <span className="stock-label">{stockLabel}</span>
              </div>
            </div>

            {/* Selector de Cantidad */}
            {!isOutOfStock && (
              <div className="quantity-section">
                <label>Cantidad:</label>
                <div className="quantity-controls">
                  <button
                    className="quantity-btn"
                    onClick={() => handleQuantityChange(-1)}
                    disabled={quantity <= 1}
                  >
                    <Minus size={16} />
                  </button>
                  <span className="quantity-display">{quantity}</span>
                  <button
                    className="quantity-btn"
                    onClick={() => handleQuantityChange(1)}
                    disabled={quantity >= maxQuantity}
                  >
                    <Plus size={16} />
                  </button>
                </div>
              </div>
            )}

            {/* Total y Botón de Compra */}
            <div className="purchase-section">
              {!isOutOfStock && (
                <div className="total-price">
                  <span>Total: <strong>${totalPrice.toLocaleString()}</strong></span>
                </div>
              )}
              
              <button
                className={`add-to-cart-modal-btn ${isOutOfStock ? "disabled" : ""}`}
                onClick={handleAddToCart}
                disabled={isOutOfStock}
              >
                {isOutOfStock ? (
                  <>
                    <span>Producto Agotado</span>
                  </>
                ) : (
                  <>
                    <ShoppingCart size={20} />
                    <span>Agregar al Carrito</span>
                  </>
                )}
              </button>

              {isOutOfStock && (
                <p className="out-of-stock-message">
                  Este producto se encuentra temporalmente sin stock. 
                  Te notificaremos cuando esté disponible nuevamente.
                </p>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};