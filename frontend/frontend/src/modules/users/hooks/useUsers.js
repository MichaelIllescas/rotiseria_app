// src/hooks/useUsers.js
import { useState, useEffect, useCallback } from "react";
import { list as listUsers } from "../services/userService";

export default function useUsers(initialParams = null) {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchUsers = useCallback(
    async (params = initialParams) => {
      setLoading(true);
      setError(null);
      try {
        const data = await listUsers(params);
        setUsers(data);
        return data;
      } catch (err) {
        setError(err?.message || "Error fetching users");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    [initialParams]
  );

  // 🚀 cargar automáticamente al montar el hook
  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  return {
    users,
    loading,
    error,
    refetch: fetchUsers,
  };
}
