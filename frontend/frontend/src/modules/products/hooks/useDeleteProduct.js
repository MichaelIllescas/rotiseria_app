// hooks/useDeleteProduct.js
import { useState } from "react";
import { remove } from '../services/productService';
import { toast } from '../../../ui/toaster';

export const useDeleteProduct = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const deleteProduct = async (productId) => {
    setLoading(true);
    try {
      await remove(productId);
      toast.success("Producto eliminado con éxito");
      setError(null);
      // Opcional:
    } catch (err) {
      setError(err.message);
      toast.error(`Error eliminando producto: ${err.message}`);
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
