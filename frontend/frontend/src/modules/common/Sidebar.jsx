import { useState, useEffect } from "react";
import { Link, useLocation } from "react-router-dom";
import {
  LayoutDashboard,
  Package,
  Folder,
  Settings,
  Menu,
  ArrowLeft,
  User
} from "lucide-react";
import "./styles/sidebar.css";

//  Array con tus items de navegación
const menuItems = [
  { name: "Panel de Control", path: "/dashboard", icon: LayoutDashboard },
  { name: "Productos", path: "/products", icon: Package },
  { name: "Categorías", path: "/categories", icon: Folder },
  { name: "Configuración", path: "/settings", icon: Settings },
  { name: "Usuarios", path: "/users", icon: User   },
];

export default function Sidebar() {
  const [isOpen, setIsOpen] = useState(false);
  const [isDesktop, setIsDesktop] = useState(false);
  const location = useLocation(); 
  // Detectar si estamos en desktop
  useEffect(() => {
    const checkScreenSize = () => {
      setIsDesktop(window.innerWidth >= 768);
      // En desktop, inicializar como abierto
      if (window.innerWidth >= 768) {
        setIsOpen(true);
      }
    };

    checkScreenSize();
    window.addEventListener('resize', checkScreenSize);
    
    return () => window.removeEventListener('resize', checkScreenSize);
  }, []);

  return (
    <>
      {/* Overlay para móviles */}
      <div 
        className={`sidebar-overlay ${isOpen && !isDesktop ? "show" : ""}`}
        onClick={() => setIsOpen(false)}
      />

      {/* Botón hamburguesa (siempre visible) */}
      <button
        className="sidebar-toggle"
        onClick={() => setIsOpen(!isOpen)}
      >
        {isOpen ? <ArrowLeft /> : <Menu />}
      </button>

      {/* Sidebar */}
      <aside className={`sidebar ${isOpen ? "open" : ""} ${isDesktop ? "desktop" : ""}`}>
        <div className="sidebar-header">
          <span className="logo">🍗 FoodStore Admin</span>
          <span className="subtitle">Sistema de administración</span>
        </div>

        <nav className="sidebar-nav">
          {menuItems.map(({ name, path, icon: Icon }) => (
            <Link
              key={path}
              to={path}
              className={location.pathname === path ? "active" : ""}
              onClick={() => !isDesktop && setIsOpen(false)} // 👈 Solo cierra en mobile
            >
              <Icon />
              <span>{name}</span>
            </Link>
          ))}
        </nav>
      </aside>
    </>
  );
}
