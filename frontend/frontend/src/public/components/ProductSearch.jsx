import React from "react";
import "../styles/ProductSearch.css";

export function ProductSearch({ searchTerm, onSearchChange }) {
  return (
    <div className="product-search">
      <input
        type="text"
        placeholder="Buscar productos..."
        value={searchTerm}
        onChange={(e) => onSearchChange(e.target.value)}
        className="search-input"
      />
    </div>
  );
}
