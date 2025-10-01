// hooks/useUpdateProduct.js
import { useState } from "react";
import { update } from '../services/productService';

export const useUpdateProduct = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const updateProduct = async (productId, productData) => {
    setLoading(true);
    try {
      await update(productId, productData);
      // Aquí puedes manejar la respuesta o el estado después de actualizar el producto
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return {
    loading,
    error,
    updateProduct,
  };
};
