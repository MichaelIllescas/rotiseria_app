import { useState } from 'react';
import { update } from '../services/categoryService';


export function useUpdateCategory() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const updateCategory = async (id, categoryData) => {
    try {
      setLoading(true);
      setError(null);
      const result = await update(id, categoryData);
      return result;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    updateCategory,
    loading,
    error,
  };
}
