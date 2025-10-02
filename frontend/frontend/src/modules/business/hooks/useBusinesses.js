import { useState, useEffect } from 'react';
import { list } from '../services/businessService';

export function useBusinesses() {
  const [businesses, setBusinesses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchBusinesses = async () => {
    try {
      setLoading(true);
      const data = await list();
      setBusinesses(data);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBusinesses();
  }, []);

  return {
    businesses,
    loading,
    error,
    refetchBusinesses: fetchBusinesses,
  };
}