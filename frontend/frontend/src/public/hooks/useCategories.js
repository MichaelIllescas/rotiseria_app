// src/modules/category/hooks/useCategories.js (VERSIÓN TEMPORAL PARA PRUEBAS)

import { useState, useEffect } from 'react';
import { listActivesCategories } from '../services/categoryService';
export function useCategories() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchCategories = async () => {
    try {
      setLoading(true);
       const data = await listActivesCategories();
       setCategories(data);
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