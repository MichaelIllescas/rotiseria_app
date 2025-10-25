import React, { useState } from "react";
import { ShoppingCart, Eye } from "lucide-react";
import "../styles/ProductCard.css";
import { useCart } from "../../context/CartContext";

export const ProductCard = ({ product, onAddToCart, onViewDetail }) => {
  const { name, description, price, imageUrl, dailyStock } = product;

    const { addToCart } = useCart();


  // Estado para definir dinámicamente el modo de ajuste
  const [fitMode, setFitMode] = useState("cover");

  // Detecta proporción al cargar la imagen
  const handleImageLoad = (e) => {
    const img = e.target;
    const ratio = img.naturalWidth / img.naturalHeight;

    // Si es más apaisada => cover, si es más cuadrada o alta => contain
    if (ratio > 1.3) setFitMode("cover");
    else setFitMode("contain");
  };

  const stockLabel =
    dailyStock > 0 ? `${dailyStock} disponibles` : "Sin stock";

  return (
    <div className={`product-card ${dailyStock === 0 ? "out-of-stock" : ""}`}>
      <div className="product-image" onClick={() => onViewDetail(product)}>
        <img
          src={
            imageUrl?.startsWith("http")
              ? imageUrl
              : `${import.meta.env.VITE_IMAGE_BASE_URL || "http://localhost:8080"}${imageUrl}`
          }
          alt={name}
          loading="lazy"
          onLoad={handleImageLoad}
          style={{ objectFit: fitMode }}
        />
        <div className="image-overlay">
          <Eye size={24} />
          <span>Ver detalle</span>
        </div>
      </div>

      <div className="product-content">
        <h3 className="product-name" onClick={() => onViewDetail(product)}>{name}</h3>
        <p className="product-description">{description}</p>

        <div className="product-footer">
          <div className="product-info">
            <span className="product-price">${price.toLocaleString()}</span>
            <span
              className={`product-stock ${
                dailyStock > 0 ? "available" : "unavailable"
              }`}
            >
              {stockLabel}
            </span>
          </div>

          <button
            className="add-to-cart-btn"
            disabled={dailyStock === 0}
            onClick={() => addToCart(product)}
          >
            <ShoppingCart size={18} />
            <span>Agregar</span>
          </button>
        </div>
      </div>
    </div>
  );
};
