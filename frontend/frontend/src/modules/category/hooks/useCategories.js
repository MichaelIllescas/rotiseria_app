// src/modules/category/hooks/useCategories.js (VERSIÓN TEMPORAL PARA PRUEBAS)

import { useState, useEffect } from 'react';
import { mockCategories } from '../data/mockData'; // Importamos los datos de prueba

export function useCategories() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchCategories = async () => {
    try {
      setLoading(true);
      // Simulamos un retraso de red de 1 segundo para ver el estado de "cargando"
      await new Promise(resolve => setTimeout(resolve, 10));
      
      // "Cargamos" los datos de prueba
      setCategories(mockCategories);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  return {
    categories,
    loading,
    error,
    refetchCategories: fetchCategories, // Permitirá recargar los datos de prueba
  };
}