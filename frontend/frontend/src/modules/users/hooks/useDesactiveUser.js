// hook para activar/desactivar un usuario
import { toggleDesactive } from "../services/userService";
import { useState, useCallback } from "react";

export default function useDesactiveUser() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [data, setData] = useState(null);

  const desactivateUser = useCallback(async (userId) => {
    setLoading(true);
    setError(null);
    try {
      const response = await toggleDesactive(userId);
      setData(response?.data || response); // guarda solo los datos útiles
      return response?.data || response;   // también devolvelo al que lo llame
    } catch (err) {
      setError(err?.message || "Error al desactivar usuario");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    loading,
    error,
    data,
    desactivateUser,
  };
}
