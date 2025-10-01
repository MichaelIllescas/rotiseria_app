  // src/context/AuthContext.jsx
  import { createContext, useContext, useEffect, useState } from "react";
  import apiClient from "../shared/services/apiClient";

  const AuthContext = createContext();

  export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null); // guardamos los datos del usuario
    const [loading, setLoading] = useState(true);


  const getUserSession = async () => {
    try {
      const { data } = await apiClient.get("/api/users/me", { withCredentials: true });
      setUser(data);
    } catch (err) {
      setUser(null); // si no está autenticado
    } finally {
      setLoading(false);
    }
  };

  // 👇 Llamamos una sola vez al montar el provider
  useEffect(() => {
    getUserSession();
  }, []);

    // Nueva función para manejar login
    const login = () => {
      window.location.href = "http://localhost:8080/login";
    };

    // Nueva función para manejar logout
    const logout = async () => {
      try {
        await apiClient.post("/logout");
        setUser(null);
      } catch (error) {
        // Incluso si falla el logout en el backend, limpiamos el estado local
        setUser(null);
      }
    };

    return (
      <AuthContext.Provider value={{ 
        user, 
        setUser, 
        loading, 
        login, 
        logout,
        getUserSession
      }}>
        {children}
      </AuthContext.Provider>
    );
  };

  export const useAuth = () => useContext(AuthContext);