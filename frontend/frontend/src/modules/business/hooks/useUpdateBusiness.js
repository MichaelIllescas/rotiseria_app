import { useState } from 'react';
import { update } from '../services/businessService';

export function useUpdateBusiness() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const updateBusiness = async (businessId, businessData) => {
    try {
      setLoading(true);
      setError(null);
      const result = await update(businessId, businessData);
      return result;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    updateBusiness,
    loading,
    error,
  };
}