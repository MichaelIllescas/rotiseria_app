import { useState, useCallback } from "react";
import { register as registerUser } from "../services/userService";
export default function useRegister() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [data, setData] = useState(null);

  const register = useCallback(async (payload, options = {}) => {
    setLoading(true);
    setError(null);
    try {
      const result = await registerUser(payload, options);
      setData(result);
      return result;
    } catch (err) {
      const message = err?.message || "Registration failed";
      setError(message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const clearError = () => setError(null);

  return {
    register,
    loading,
    error,
    data,
    clearError,
  };
}