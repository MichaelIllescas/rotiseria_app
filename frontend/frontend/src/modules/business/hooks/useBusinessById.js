import { useState } from 'react';
import { getById } from '../services/businessService';

export function useBusinessById() {
  const [business, setBusiness] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchBusiness = async (businessId) => {
    try {
      setLoading(true);
      setError(null);
      const data = await getById(businessId);
      setBusiness(data);
      return data;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const clearBusiness = () => {
    setBusiness(null);
    setError(null);
  };

  return {
    business,
    loading,
    error,
    fetchBusiness,
    clearBusiness,
  };
}