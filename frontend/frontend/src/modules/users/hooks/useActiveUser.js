// hook para activar/desactivar un usuario
import { toggleActive } from "../services/userService";
import { useState, useCallback } from "react";

export default function useActiveUser() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [data, setData] = useState(null);

  const activateUser = useCallback(async (userId) => {
    setLoading(true);
    setError(null);
    try {
      const response = await toggleActive(userId);
      setData(response?.data || response); // guarda solo los datos útiles
      return response?.data || response;   // también devolvelo al que lo llame
    } catch (err) {
      setError(err?.message || "Error al activar usuario");
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    loading,
    error,
    data,
    activateUser,
  };
}
