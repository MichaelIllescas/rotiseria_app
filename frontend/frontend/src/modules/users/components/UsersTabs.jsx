import { useState } from "react";
import { UsersList } from "./UsersList";
import { UserRegistrationForm } from "./UserRegistrationForm";
import "../styles/usersTabs.css";
import useRegister from "../hooks/useRegister";
import { toast } from "../../../ui/toaster";
import useUsers from "../hooks/useUsers";
import useActiveUser from "../hooks/useActiveUser";
import useDesactiveUser from "../hooks/useDesactiveUser";


export function UsersTabs() {
  const [activeTab, setActiveTab] = useState("list");
  const { register, loading: registering, error: registerError } = useRegister();
 const { users, loading: usersLoading, error: usersError, refetch } = useUsers();
 const { activateUser, loading: activating, error: activateError } = useActiveUser();
 const { desactivateUser, loading: desactivating, error: desactivateError } = useDesactiveUser();

 const handleToggleStatus = async (user) => {
  try {
    if (user.active) {
      // Si está activo → desactivarlo
      await desactivateUser(user.id);
      toast.success("Usuario desactivado correctamente");
    } else {
      // Si está inactivo → activarlo
      await activateUser(user.id);
      toast.success("Usuario activado correctamente");
    }

    // Refrescar la lista de usuarios
    refetch();
  } catch (err) {
    toast.error(err?.message || "Error al actualizar estado del usuario");
  }
};


  const handleUpdateUser = async (userId, userData) => {
    console.log("Update user:", userId, userData);
  };

 const handleRegisterUser = async (formData) => {
    try {
      // llama al hook/service que creaste
      await register(formData);
      toast.success("Usuario registrado correctamente");
      // opcional: volver al listado
      setActiveTab("list");
    } catch (err) {
      // registerError ya contiene el mensaje, pero mostramos por si acaso
      toast.error(err?.message || registerError || "Error al registrar usuario");
      throw err; // si quieres que el form maneje limpieza/errores
    }
  };

  return (
    <div className="tabs-container">
      {/* Tabs header */}
       <div >
      {/* Párrafo arriba */}
      <h2>Gestion de usuarios</h2>
      <p className="text-muted-foreground" style={{ marginBottom: "1rem" }}>
        Gestiona los usuarios del sistema, asigna roles y controla accesos
      </p>

      {/* Tabs */}
      <div className="tabs">
        <button
          className={`tab ${activeTab === "list" ? "active" : ""}`}
          onClick={() => setActiveTab("list")}
        >
          Listado de Usuarios
        </button>
        <button
          className={`tab ${activeTab === "register" ? "active" : ""}`}
          onClick={() => setActiveTab("register")}
        >
          Registrar Usuario
        </button>
      </div>

      {/* Tabs content */}
      <div className="">
        {activeTab === "list" && (
          <UsersList
            users={users}
            onToggleStatus={handleToggleStatus}
            onUpdateUser={handleUpdateUser}
            refetchUsers={refetch}
          />
        )}

        {activeTab === "register" && (
          <UserRegistrationForm onSubmit={handleRegisterUser} />
        )}
      </div>
    </div>
    </div>
  );
}
