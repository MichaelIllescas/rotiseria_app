
import { useState, useEffect } from 'react';
import { getBusinessData } from '../services/businessService';


export function useBusinessData() {
  const [businessData, setBusinessData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchBusinessData = async () => {
    try {
      setLoading(true);
       const data = await getBusinessData();
       setBusinessData(data);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBusinessData();
  }, []);

  return {
    businessData,
    loading,
    error,
    refetchBusinessData: fetchBusinessData, // Permitirá recargar los datos de prueba
  };
}