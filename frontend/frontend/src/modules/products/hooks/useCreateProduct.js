//hooks/useCreateProduct.js
import { useState } from "react";
import {create} from '../services/productService';

export const useCreateProduct = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const createProduct = async (productData) => {
    setLoading(true);
    try {
      await create(productData);
      // Aquí puedes manejar la respuesta o el estado después de crear el producto
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return {
    loading,
    error,
    createProduct,
  };
};
