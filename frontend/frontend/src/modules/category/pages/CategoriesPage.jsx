// pages/CategoriesPage.js
import React from 'react';
import { CategoriesList } from '../components/CategoriesList';
import '../styles/categoriesPage.css';

export const CategoriesPage = () => {
  return (
    <div className="categories-page">
      <h1 className="page-title">Gestión de Categorías</h1>
      <p className="page-description">Administra las categorías de productos aquí.</p>
      <CategoriesList />
    </div>
  );
}