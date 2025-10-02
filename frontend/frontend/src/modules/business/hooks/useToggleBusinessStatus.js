import { useState } from 'react';
import { toggleStatus } from '../services/businessService';

export function useToggleBusinessStatus() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const toggleBusinessStatus = async (businessId, isActive) => {
    try {
      setLoading(true);
      setError(null);
      const result = await toggleStatus(businessId, { active: isActive });
      return result;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    toggleBusinessStatus,
    loading,
    error,
  };
}