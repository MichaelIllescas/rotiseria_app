import Sidebar from "../modules/common/Sidebar";
import { UserHeader } from "../modules/users/components/UserHeader";
import { useAuth } from "../context/AuthContext";
import  PrivateNavbar  from "../modules/common/PrivateNavbar";

export const MainLayout = ({ children }) => {
  const { logout } = useAuth();

  return (
    <div
      style={{
        color: "inherit",
        display: "flex",
        minHeight: "100dvh",
        overflow: "hidden"
      }}
    >
      {/* === Sidebar fijo === */}

      {/* === Contenedor principal === */}
      <div
        style={{
          flex: 1,
          display: "flex",
          flexDirection: "column",
        }}
      >
        {/* Header superior */}
        <header style={{  zIndex: 10 }}>
          <PrivateNavbar/>
        </header>

        {/* Contenido dinámico */}
        <main
          style={{
            flex: 1,
            padding: "1rem",
            overflowY: "auto",
          }}
        >
          {children}
        </main>

        {/* Footer */}
        <footer
          style={{
            textAlign: "center",
            padding: "1rem 2rem",
            fontSize: "0.875rem",
            color: "#666",
            borderTop: "1px solid #eee",
          }}
        >
          © 2025 Rotisería Admin
        </footer>
      </div>
    </div>
  );
};
