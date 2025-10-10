import React, { useState } from "react";
import { ProductCard } from "./ProductCard";
import { groupProductsByCategory } from '../helper/groupProductsByCategory';
import "../styles/ProductTabs.css";

export const ProductTabs = ({ categories = [] , products = [] }) => {
  // Recibir categories como prop destructurada con valor por defecto

 
  // Agrupamos productos según su categoría
  const groupedCategories = groupProductsByCategory(categories, products);
  const [activeTab, setActiveTab] = useState(groupedCategories[0]?.name || "");

  // Validar que categories sea un array válido antes de renderizar
  if (!Array.isArray(categories) || categories.length === 0) {
    return (
      <div className="product-tabs">
        <p>Cargando categorías...</p>
      </div>
    );
  }

  return (
    <div className="product-tabs">
      {/* Tabs header */}
      <div className="tabs-header">
        {groupedCategories.map((cat) => (
          <button
            key={cat.id}
            className={`tab-btn ${activeTab === cat.name ? "active" : ""}`}
            onClick={() => setActiveTab(cat.name)}
          >
            {cat.name}
          </button>
        ))}
      </div>

      {/* Contenido */}
      <div className="tabs-content-wrapper">
        <div className="tabs-content" key={activeTab}>
          {groupedCategories
            .find((cat) => cat.name === activeTab)
            ?.products.map((product) => (
              
              <ProductCard
                key={product.id}
                product={product}
                onAddToCart={() =>
                  console.log(`Agregado al carrito: ${product.name}`)
                }
              />
            ))}
        </div>
      </div>
    </div>
  );
};
