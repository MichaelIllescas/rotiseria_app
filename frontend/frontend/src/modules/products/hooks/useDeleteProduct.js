// hooks/useDeleteProduct.js
import { useState } from "react";
import { deleteProduct } from '../services/productService';
export const useDeleteProduct = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const deleteProduct = async (productId) => {
    setLoading(true);
    try {
      await deleteProduct(productId);
      // Aquí puedes manejar la respuesta o el estado después de eliminar el producto
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return {
    loading,
    error,
    deleteProduct,
  };
};
