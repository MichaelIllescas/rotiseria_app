import { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import {
  LayoutDashboard,
  Package,
  Folder,
  Settings,
  Menu,
  X,
  User
} from "lucide-react";
import "./styles/sidebar.css";

// 📌 Array con tus items de navegación
const menuItems = [
  { name: "Panel de Control", path: "/dashboard", icon: LayoutDashboard },
  { name: "Productos", path: "/products", icon: Package },
  { name: "Categorías", path: "/categories", icon: Folder },
  { name: "Configuración", path: "/settings", icon: Settings },
  { name: "Usuarios", path: "/users", icon: User   },
];

export default function Sidebar() {
  const [isOpen, setIsOpen] = useState(false);
  const location = useLocation(); // 👈 Para resaltar el activo

  return (
    <>
      {/* Botón hamburguesa (solo mobile) */}
      <button
        className="sidebar-toggle"
        onClick={() => setIsOpen(!isOpen)}
      >
        {isOpen ? <X /> : <Menu />}
      </button>

      {/* Sidebar */}
      <aside className={`sidebar ${isOpen ? "open" : ""}`}>
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
              onClick={() => setIsOpen(false)} // 👈 Cierra el menú al navegar en mobile
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
