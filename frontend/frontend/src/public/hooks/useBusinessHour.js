
import { useState, useEffect } from 'react';
import { listBusinessHours } from '../services/businessHourService';


export function useBusinessHours() {
  const [businessHours, setBusinessHours] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchBusinessHours = async () => {
    try {
      setLoading(true);
       const data = await listBusinessHours();
       setBusinessHours(data);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBusinessHours();
  }, []);

  return {
    businessHours,
    loading,
    error,
    refetchBusinessHours: fetchBusinessHours, // Permitirá recargar los datos de prueba
  };
}