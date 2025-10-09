import Sidebar from "../modules/common/Sidebar";
import { UserHeader } from "../modules/users/components/UserHeader";
import { useAuth } from "../context/AuthContext";

export const MainLayout = ({ children }) => {
  const { logout } = useAuth();
  return (
    <div>
      {/* === Sidebar fijo en desktop / deslizable en mobile === */}
      <Sidebar />

      {/* === Contenedor principal === */}
      <div>
        {/* Header superior */}
        <header>
          <UserHeader onLogout={logout} />
        </header>

        {/* Contenido dinámico */}
        <main>
          {children}
        </main>

        {/* Footer (opcional) */}
        <footer style={{ textAlign: 'center', padding: '1rem', fontSize: '0.875rem', color: '#888' }}>
          © 2025 Rotisería Admin
        </footer>
      </div>
    </div>
  );
};
