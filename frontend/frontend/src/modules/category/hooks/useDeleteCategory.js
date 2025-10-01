import { useState } from 'react';
import { deleteCategory } from '../services/categoryService';

export function useDeleteCategory() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const deleteCategory = async (id) => {
    try {
      setLoading(true);
      setError(null);
      const result = await deleteCategory(id);
      return result;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    deleteCategory,
    loading,
    error,
  };
}
