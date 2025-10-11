import React, { useState } from "react";
import { ProductCard } from "./ProductCard";
import { groupProductsByCategory } from "../helper/groupProductsByCategory";
import "../styles/ProductTabs.css";

export const ProductTabs = ({ categories = [], products = [], searchTerm = "" }) => {
  // Agrupamos productos según su categoría (todos los productos originales)
  const groupedCategories = groupProductsByCategory(categories, products);
  const [activeTab, setActiveTab] = useState(groupedCategories[0]?.name || "");

  // Detectar si hay una búsqueda activa
  const isSearching = searchTerm.trim().length > 0;

  // Si hay búsqueda, filtramos TODOS los productos
  // Si no hay búsqueda, mostramos los productos de la categoría activa
  const productsToShow = isSearching
    ? products.filter((product) =>
        product.name.toLowerCase().includes(searchTerm.toLowerCase())
      )
    : groupedCategories.find((cat) => cat.name === activeTab)?.products || [];

  return (
    <div className="product-tabs">
      {/* Tabs header — solo se muestran si NO hay búsqueda */}
      {!isSearching ? (
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
      ) : (
        <div className="search-results-header">
          <h3>
            Resultados de búsqueda ({productsToShow.length} productos encontrados)
          </h3>
        </div>
      )}

      {/* Contenido */}
      <div className="tabs-content-wrapper">
        <div className="tabs-content" key={isSearching ? "search" : activeTab}>
          {productsToShow.length > 0 ? (
            productsToShow.map((product) => (
              <ProductCard
                key={product.id}
                product={product}
                onAddToCart={() =>
                  console.log(`Agregado al carrito: ${product.name}`)
                }
              />
            ))
          ) : (
            <p>
              {isSearching
                ? "No se encontraron productos que coincidan con tu búsqueda."
                : "No hay productos en esta categoría."}
            </p>
          )}
        </div>
      </div>
    </div>
  );
};
