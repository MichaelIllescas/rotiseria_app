import { useState } from 'react';
import { deleteBusiness as deleteBusinessService } from '../services/businessService';

export function useDeleteBusiness() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleDeleteBusiness = async (businessId) => {
    try {
      setLoading(true);
      setError(null);
      const result = await deleteBusinessService(businessId);
      return result;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    deleteBusiness: handleDeleteBusiness,
    loading,
    error,
  };
}