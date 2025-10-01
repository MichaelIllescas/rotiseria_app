import React from 'react';
import { ProductsList } from '../components/ProductsList';
import "../styles/productsPage.css";

export function ProductsPage() {
  return (
  <div className="products-page-container">
    <h1 className="products-page-title">Gestión de Productos</h1>
    <p className="products-page-description">Aquí puedes gestionar todos los productos de la tienda.</p>
  <ProductsList />

  </div>
) }
export default ProductsPage;