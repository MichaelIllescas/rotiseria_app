// hooks/useToggleProductStatus.js
import { useState } from "react";
import { toggleStatus } from '../services/productService';

export const useToggleProductStatus = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const toggleProductStatus = async (productId, currentStatus) => {
    setLoading(true);
    try {
      await toggleStatus(productId, { active: !currentStatus });
      // Aquí puedes manejar la respuesta o el estado después de cambiar el estado del producto
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return {
    loading,
    error,
    toggleProductStatus,
  };
};
