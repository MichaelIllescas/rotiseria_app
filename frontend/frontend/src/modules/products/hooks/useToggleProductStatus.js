// hooks/useToggleProductStatus.js
import { useState } from "react";
import { toggleStatus } from '../services/productService';
import { toast } from '../../../ui/toaster';


export const useToggleProductStatus = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const toggleProductStatus = async (productId) => {
    setLoading(true);
    try {
      await toggleStatus(productId);
      setError(null);
      toast.success("Estado del producto cambiado con éxito");
      // Aquí puedes manejar la respuesta o el estado después de cambiar el estado del producto
    } catch (err) {
      setError(err.message);
      toast.error(`Error al cambiar el estado del producto: ${err.message}`);
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
