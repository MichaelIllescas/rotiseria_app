import React, { useState } from "react";
import { ShoppingCart } from "lucide-react";
import "../styles/ProductCard.css";

export const ProductCard = ({ product, onAddToCart }) => {
  const { name, description, price, imageUrl, dailyStock } = product;

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
      <div className="product-image">
        <img
          src={
            imageUrl?.startsWith("http")
              ? imageUrl
              : `${"http://localhost:8080"}${imageUrl}`
          }
          alt={name}
          loading="lazy"
          onLoad={handleImageLoad}
          style={{ objectFit: fitMode }}
        />
      </div>

      <div className="product-content">
        <h3 className="product-name">{name}</h3>
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
            onClick={() => onAddToCart(product)}
          >
            <ShoppingCart size={18} />
            <span>Agregar</span>
          </button>
        </div>
      </div>
    </div>
  );
};
