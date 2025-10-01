import { useState, useEffect } from 'react';
import { list } from '../services/productService';
import { mockProducts } from '../data/mockData';

export function useProducts() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchProducts = async () => {
    try {
      setLoading(true);
          // Simulamos un retraso de red de 1 segundo para ver el estado de "cargando"
          await new Promise(resolve => setTimeout(resolve, 10));
          
          // "Cargamos" los datos de prueba
          setProducts(mockProducts);
      
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  return {
    products,
    loading,
    error,
    refetchProducts: fetchProducts,
  };
}