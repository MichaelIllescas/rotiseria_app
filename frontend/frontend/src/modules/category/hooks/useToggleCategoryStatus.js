import { useState } from 'react';
import { toggleStatus } from '../services/categoryService';

export function useToggleCategoryStatus() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const toggleCategoryStatus = async (id, isActive) => {
    try {
      setLoading(true);
      setError(null);
      const result = await toggleStatus(id, isActive);
      return result;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    toggleCategoryStatus,
    loading,
    error,
  };
}
