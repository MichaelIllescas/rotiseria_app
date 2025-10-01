import { useState } from 'react';
import { deleteCategory as deleteCategoryService } from '../services/categoryService';

export function useDeleteCategory() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleDeleteCategory = async (id) => {
    try {
      setLoading(true);
      setError(null);
      const result = await deleteCategoryService(id); // ✅ llamamos al service real
      return result;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    deleteCategory: handleDeleteCategory, // lo exponemos con este nombre
    loading,
    error,
  };
}
