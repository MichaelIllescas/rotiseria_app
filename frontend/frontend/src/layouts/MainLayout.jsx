import Sidebar from "../modules/common/Sidebar";
import { UserHeader } from "../modules/users/components/UserHeader";

export const MainLayout = ({ children }) => {
  return (
    <div className="flex min-h-screen">
      {/* === Sidebar fijo en desktop / deslizable en mobile === */}
      <Sidebar />

      {/* === Contenedor principal === */}
      <div className="flex flex-col flex-1 ml-0 md:ml-64 transition-all">
        {/* Header superior */}
        <header className="p-4 border-b bg-white">
          <UserHeader />
        </header>

        {/* Contenido dinámico */}
        <main id="main" role="main" className="flex-grow p-6 bg-gray-50">
          {children}
        </main>

        {/* Footer (opcional) */}
        <footer aria-hidden="true" className="p-4 text-center text-sm text-gray-500">
          © 2025 Rotisería Admin
        </footer>
      </div>
    </div>
  );
};
