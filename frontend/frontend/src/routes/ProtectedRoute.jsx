import { useAuth } from "../context/AuthContext";
import { MainLayout } from "../layouts/MainLayout";

export const ProtectedRoute = ({ element }) => {
  const { user, loading } = useAuth();

  if (loading) return null; // Espera a que la sesión cargue

  return user ? <MainLayout>{element}</MainLayout> : window.location.href = "http://localhost:8080/login";
};
