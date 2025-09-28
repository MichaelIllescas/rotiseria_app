import { useAuth } from "../context/AuthContext";
import PrivateNavbar from "../modules/common/PrivateNavbar";

export const MainLayout = ({ children }) => {
  const { logout } = useAuth();

  return (
    <div className="flex flex-col min-h-screen">
      <header>
        <PrivateNavbar />
      </header>

      <main id="main" role="main" className="flex-grow p-4">
        {children}
      </main>

      <footer aria-hidden="true" />
    </div>
  );
};

export default MainLayout;
