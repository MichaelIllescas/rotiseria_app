import { useState, useEffect } from "react";
import { Link, useLocation } from "react-router-dom";
import {
  LayoutDashboard,
  Package,
  Folder,
  Settings,
  User,
  Menu,
  ChevronDown,
  UserCog,
  LogOut,
} from "lucide-react";
import { useAuth } from "../../context/AuthContext";
import useUpdateUser from "../users/hooks/useUpdateUser";
import useChangePassword from "../users/hooks/useChangePassword";
import "./styles/privateNavbar.css";

const menuItems = [
  { name: "Panel", path: "/dashboard", icon: LayoutDashboard },
  { name: "Productos", path: "/products", icon: Package },
  { name: "Categorías", path: "/categories", icon: Folder },
  { name: "Configuración", path: "/settings", icon: Settings },
  { name: "Usuarios", path: "/users", icon: User },
];

export default function PrivateNavbar() {
  const { user, logout, setUser } = useAuth();
  const location = useLocation();
  const [isMobile, setIsMobile] = useState(false);
  const [openMenu, setOpenMenu] = useState(false);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  // perfil / password / logout modales
  const [isProfileDialogOpen, setIsProfileDialogOpen] = useState(false);
  const [isPasswordDialogOpen, setIsPasswordDialogOpen] = useState(false);
  const [showLogoutConfirm, setShowLogoutConfirm] = useState(false);

  const { updateUser } = useUpdateUser();
  const { changePassword } = useChangePassword();

  const [formData, setFormData] = useState({
    name: "",
    lastname: "",
    email: "",
    role: "",
  });
  const [isSaving, setIsSaving] = useState(false);
  const [saveError, setSaveError] = useState(null);

  const [passwordForm, setPasswordForm] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });
  const [passwordError, setPasswordError] = useState(null);
  const [isChangingPassword, setIsChangingPassword] = useState(false);

  useEffect(() => {
    const handleResize = () => setIsMobile(window.innerWidth < 768);
    handleResize();
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  const getInitials = (name, lastname) =>
    name && lastname
      ? `${name.charAt(0)}${lastname.charAt(0)}`.toUpperCase()
      : "?";

  const toggleMenu = () => setOpenMenu(!openMenu);
  const toggleDropdown = () => setIsDropdownOpen(!isDropdownOpen);

  const handleChange = (e) =>
    setFormData((f) => ({ ...f, [e.target.name]: e.target.value }));

  const handlePasswordInputChange = (e) =>
    setPasswordForm((f) => ({ ...f, [e.target.name]: e.target.value }));

  const handleSaveProfile = async (e) => {
    e.preventDefault();
    setIsSaving(true);
    setSaveError(null);

    if (!formData.name || !formData.lastname || !formData.email) {
      setSaveError("Nombre, apellido y email son requeridos.");
      setIsSaving(false);
      return;
    }

    try {
      const payload = {
        id: user?.id,
        name: formData.name,
        lastname: formData.lastname,
        email: formData.email,
        role: formData.role ?? user?.role ?? "ATENCION",
      };

      const updatedUser = await updateUser(payload);
      setUser(updatedUser);
      setIsProfileDialogOpen(false);
    } catch (err) {
      setSaveError(err?.message || "Error al actualizar el perfil.");
    } finally {
      setIsSaving(false);
    }
  };

  const handleChangePasswordSubmit = (e) => {
    e.preventDefault();
    setPasswordError(null);
    const { currentPassword, newPassword, confirmPassword } = passwordForm;

    if (!currentPassword || !newPassword || !confirmPassword) {
      setPasswordError("Todos los campos son requeridos.");
      return;
    }
    if (newPassword !== confirmPassword) {
      setPasswordError("La nueva contraseña y su confirmación no coinciden.");
      return;
    }

    setIsChangingPassword(true);
    changePassword(user?.id, { newPassword });
    setTimeout(() => {
      setIsChangingPassword(false);
      setIsPasswordDialogOpen(false);
    }, 800);
  };

  return (
    <header className="private-navbar">
      {/* Logo + menú principal */}
      <div className="navbar-left">
        <button className="menu-btn" onClick={toggleMenu}>
          <Menu size={22} />
        </button>
        <span className="navbar-logo">🍗 FoodStore Admin</span>
        <nav className={`navbar-links ${isMobile && !openMenu ? "hidden" : ""}`}>
          {menuItems.map(({ name, path, icon: Icon }) => (
            <Link
              key={path}
              to={path}
              className={`nav-item ${
                location.pathname === path ? "active" : ""
              }`}
              onClick={() => isMobile && setOpenMenu(false)}
            >
              <Icon size={18} />
              <span>{name}</span>
            </Link>
          ))}
        </nav>
      </div>

      {/* Sección usuario */}
      <div className="navbar-right">
        <button className="user-button" onClick={toggleDropdown}>
          <div className="avatar">
            {getInitials(user?.name, user?.lastname)}
          </div>
          <ChevronDown size={16} />
        </button>

        {isDropdownOpen && (
          <>
            <div className="dropdown">
              <div className="dropdown-header">
                <div className="name">
                  {user?.name} {user?.lastname}
                </div>
                <div className="email">{user?.email}</div>
              </div>

              <div className="dropdown-body">
                <button
                  className="dropdown-item"
                  onClick={() => {
                    setFormData(user);
                    setIsDropdownOpen(false);
                    setIsProfileDialogOpen(true);
                  }}
                >
                  <UserCog /> Mi Perfil
                </button>

                <button
                  className="dropdown-item"
                  onClick={() => {
                    setIsDropdownOpen(false);
                    setIsPasswordDialogOpen(true);
                  }}
                >
                  <UserCog style={{ opacity: 0.9 }} /> Cambiar Contraseña
                </button>

                <hr />
                <button
                  className="dropdown-item logout"
                  onClick={() => {
                    setIsDropdownOpen(false);
                    setShowLogoutConfirm(true);
                  }}
                >
                  <LogOut /> Cerrar Sesión
                </button>
              </div>
            </div>

            <div className="overlay" onClick={() => setIsDropdownOpen(false)} />
          </>
        )}
      </div>

      {/* === Modal Perfil === */}
      {isProfileDialogOpen && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>Mi Perfil</h3>
            <form onSubmit={handleSaveProfile} className="profile-form">
              <label>
                Nombre
                <input
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  type="text"
                />
              </label>
              <label>
                Apellido
                <input
                  name="lastname"
                  value={formData.lastname}
                  onChange={handleChange}
                  type="text"
                />
              </label>
              <label>
                Email
                <input
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  type="email"
                />
              </label>

              {saveError && <div className="error-message">{saveError}</div>}

              <div className="modal-actions">
                <button
                  type="button"
                  onClick={() => setIsProfileDialogOpen(false)}
                  className="action-cancel"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  className="action-save"
                  disabled={isSaving}
                >
                  {isSaving ? "Guardando..." : "Guardar cambios"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* === Modal Cambiar Contraseña === */}
      {isPasswordDialogOpen && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>Cambiar Contraseña</h3>
            <form onSubmit={handleChangePasswordSubmit}>
              <label>
                Contraseña Actual
                <input
                  name="currentPassword"
                  value={passwordForm.currentPassword}
                  onChange={handlePasswordInputChange}
                  type="password"
                />
              </label>
              <label>
                Nueva Contraseña
                <input
                  name="newPassword"
                  value={passwordForm.newPassword}
                  onChange={handlePasswordInputChange}
                  type="password"
                />
              </label>
              <label>
                Confirmar Nueva Contraseña
                <input
                  name="confirmPassword"
                  value={passwordForm.confirmPassword}
                  onChange={handlePasswordInputChange}
                  type="password"
                />
              </label>

              {passwordError && (
                <div className="error-message">{passwordError}</div>
              )}

              <div className="modal-actions">
                <button
                  type="button"
                  className="action-cancel"
                  onClick={() => setIsPasswordDialogOpen(false)}
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  className="action-save"
                  disabled={isChangingPassword}
                >
                  {isChangingPassword ? "Aplicando..." : "Cambiar Contraseña"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* === Modal Cerrar Sesión === */}
      {showLogoutConfirm && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>¿Cerrar Sesión?</h3>
            <p>Deberás volver a iniciar sesión para acceder al panel.</p>
            <div className="modal-actions">
              <button
                className="action-cancel"
                onClick={() => setShowLogoutConfirm(false)}
              >
                Cancelar
              </button>
              <button className="action-logout" onClick={logout}>
                Cerrar Sesión
              </button>
            </div>
          </div>
        </div>
      )}
    </header>
  );
}
