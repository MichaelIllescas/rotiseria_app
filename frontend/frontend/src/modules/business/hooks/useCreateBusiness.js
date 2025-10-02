import { useState } from 'react';
import { create } from '../services/businessService';

export function useCreateBusiness() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const createBusiness = async (businessData) => {
    try {
      setLoading(true);
      setError(null);
      const result = await create(businessData);
      return result;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    createBusiness,
    loading,
    error,
  };
}