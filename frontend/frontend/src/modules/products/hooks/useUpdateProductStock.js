import { useState } from 'react';
import { updateStock } from '../services/productService';
import { toast } from '../../../ui/toaster';


export function useUpdateProductStock() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const updateProductStock = async (productId, newStock) => {
    try {
      setLoading(true);
      setError(null);

      const result = await updateStock(productId, newStock);
      toast.success('Stock de producto actualizado con éxito');
      return result;
    } catch (err) {
      setError(err.message);
        toast.error(`Error actualizando stock: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  return { updateProductStock, loading, error };
}
