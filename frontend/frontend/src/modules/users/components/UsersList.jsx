import { useState, useEffect } from "react";
import { Edit, Users, UserCheck, UserX, RefreshCw } from "lucide-react"; // <-- añadí RefreshCw
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableCell,
} from "../../../ui/table";
import "../styles/usersList.css";
import { Button } from "../../../ui/button";
import { Input } from "../../../ui/input";
import { Label } from "../../../ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../../../ui/select";
import { Card, CardContent, CardHeader, CardTitle } from "../../../ui/card";

import useUpdateUser from "../hooks/useUpdateUser"; // <-- nuevo hook

const ITEMS_PER_PAGE = 4;

/**
 * UsersList
 * - Componente principal que renderiza las estadísticas, la tabla de usuarios,
 *   paginación y el modal de edición.
 * - Mantiene un estado local (localUsers) para permitir actualizaciones optimistas
 *   sin depender inmediatamente del prop 'users' proveniente del padre.
 * - Recibe props: users (lista), onToggleStatus (cambia activo/inactivo), onUpdateUser (callback externo).
 */
export function UsersList({ users, onToggleStatus, onUpdateUser, refetchUsers }) {
  const [currentPage, setCurrentPage] = useState(1);
  const [editingUser, setEditingUser] = useState(null);
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false);

  // local form state for modal
  const [editForm, setEditForm] = useState(null);
  const [editErrors, setEditErrors] = useState({});
  const [isSaving, setIsSaving] = useState(false);

  const { updateUser } = useUpdateUser(); // hook (prioridad)

  // copia local del listado para poder actualizar UI inmediatamente
  const [localUsers, setLocalUsers] = useState(users);

  /**
   * useEffect -> sincroniza el listado local con el prop 'users'
   * - Se ejecuta cuando cambia el prop users y actualiza localUsers.
   * - Evita que el componente quede con datos desincronizados si el padre
   *   reinyecta una nueva lista.
   */
  useEffect(() => {
    setLocalUsers(users);
  }, [users]);

  const totalPages = Math.ceil(localUsers.length / ITEMS_PER_PAGE);
  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
  const endIndex = startIndex + ITEMS_PER_PAGE;
  const currentUsers = localUsers.slice(startIndex, endIndex);

  const activeUsers = localUsers.filter((u) => u.active).length;
  const inactiveUsers = localUsers.length - activeUsers;

  /**
   * handleEditUser(user)
   * - Abre el diálogo de edición para el usuario seleccionado.
   * - Prepara el estado editForm con los valores actuales del usuario para
   *   que el modal muestre los datos a editar.
   */
  const handleEditUser = (user) => {
    setEditingUser(user);
    setEditForm({
      id: user.id ?? "",
      name: user.name ?? "",
      lastname: user.lastname ?? "",
      email: user.email ?? "",
      role: user.role ?? "ATENCION",
      active: user.active ?? true,
    });
    setEditErrors({});
    setIsEditDialogOpen(true);
  };

  /**
   * handleCloseEditDialog()
   * - Cierra el modal de edición y resetea el estado relacionado al formulario.
   * - Útil para limpiar errores y estados de guardado cuando el usuario cancela.
   */
  const handleCloseEditDialog = () => {
    setIsEditDialogOpen(false);
    setEditingUser(null);
    setEditForm(null);
    setEditErrors({});
    setIsSaving(false);
  };

  /**
   * handleEditChange(field, value)
   * - Actualiza el campo concreto del editForm localmente.
   * - Si existía un error para ese campo, lo elimina (validación reactiva).
   */
  const handleEditChange = (field, value) => {
    setEditForm((prev) => ({ ...prev, [field]: value }));
    setEditErrors((prev) => {
      if (!prev || !prev[field]) return prev;
      const next = { ...prev };
      delete next[field];
      return next;
    });
  };

  /**
   * handleSave(e)
   * - Valida el formulario de edición y realiza la llamada de actualización.
   * - Usa updateUser (hook) con prioridad, o onUpdateUser (prop) como fallback.
   * - Normaliza la respuesta para obtener el usuario actualizado y actualiza:
   *     - el listado local (setLocalUsers) para reflejar el cambio inmediatamente.
   *     - notifica al padre con onUpdateUser si está presente.
   * - Maneja errores mostrando feedback en editErrors.form.
   */
  const handleSave = async (e) => {
    e?.preventDefault?.();
    if (!editForm) return;

    // simple validation
    const errors = {};
    if (!editForm.name?.trim()) errors.name = "Nombre requerido";
    if (!editForm.lastname?.trim()) errors.lastname = "Apellido requerido";
    if (!editForm.email?.trim()) errors.email = "Email requerido";
    setEditErrors(errors);
    if (Object.keys(errors).length) return;

    setIsSaving(true);
    try {
      const payload = {
        id: editForm.id,
        name: editForm.name,
        lastname: editForm.lastname,
        email: editForm.email,
        role: editForm.role,
        active: editForm.active,
      };

      // Ejecutar el update (hook de prioridad, fallback a prop)
      let result;
      if (typeof updateUser === "function") {
        result = await updateUser(payload);
      } else if (typeof onUpdateUser === "function") {
        result = await onUpdateUser(payload);
      } else {
        throw new Error("No update handler available");
      }

      // Normalizar usuario actualizado desde la respuesta (varias formas comunes)
      const updatedUser = result?.user ?? result?.data ?? result ?? payload;

      // Actualizar listado local para reflejar cambios inmediatamente
      setLocalUsers((prev) =>
        prev.map((u) =>
          u.id === (updatedUser.id ?? payload.id) ? { ...u, ...updatedUser } : u
        )
      );

      // Notificar al padre si provee onUpdateUser (opcional) con el usuario actualizado
      if (typeof onUpdateUser === "function") {
        try {
          onUpdateUser(updatedUser);
        } catch (_) {
          /* noop */
        }
      }

      handleCloseEditDialog();
    } catch (err) {
      // Optional: show generic error
      console.error("Error updating user", err);
      setEditErrors((prev) => ({ ...prev, form: err.message || "Error al actualizar el usuario" }));
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <div className="users-list">
      {/* === Tarjetas de stats === */}
      <div className="stats-grid">
        <div className="stat-card">
          <div>
            <p className="stat-label">Total Usuarios</p>
            <p className="stat-value">{localUsers.length}</p>
          </div>
          <Users className="stat-icon" />
        </div>

        <div className="stat-card">
          <div>
            <p className="stat-label">Usuarios Activos</p>
            <p className="stat-value text-green">{activeUsers}</p>
          </div>
          <UserCheck className="stat-icon text-green" />
        </div>

        <div className="stat-card">
          <div>
            <p className="stat-label">Usuarios Inactivos</p>
            <p className="stat-value text-red">{inactiveUsers}</p>
          </div>
          <UserX className="stat-icon text-red" />
        </div>
      </div>

      {/* === Tabla de usuarios === */}
      <div className="card">
        {/* header con botón de recarga en la esquina superior derecha */}
        <div className="card-header-with-reload">
          <h2 className="card-title">Listado de Usuarios</h2>
          <button
            type="button"
            className="reload-btn"
            aria-label="Recargar listado de usuarios"
            title="Recargar"
            onClick={refetchUsers}
          >
            <RefreshCw className="reload-icon" size={16} />
          </button>
        </div>
<div className="table-wrapper">

        <Table className="table">
          <TableHeader>
            <TableRow>
              <TableHead>Nombre Completo</TableHead>
              <TableHead>Correo</TableHead>
              <TableHead>Rol</TableHead>
              <TableHead>Estado</TableHead>
              <TableHead className="text-right">Acciones</TableHead>
            </TableRow>
          </TableHeader>

          <TableBody>
            {currentUsers.length === 0 ? (
              <TableRow>
                <TableCell colSpan={6} className="text-center text-muted">
                  No hay usuarios registrados
                </TableCell>
              </TableRow>
            ) : (
              currentUsers.map((user) => (
                <TableRow key={user.id}>
                  <TableCell>
                    {user.name} {user.lastname}
                  </TableCell>
                  <TableCell>{user.email}</TableCell>
                  <TableCell>
                    <span className={`badge badge-${user.role.toLowerCase()}`}>
                      {user.role}
                    </span>
                  </TableCell>
                  <TableCell>
                    <div className="status-toggle">
                      <input
                        type="checkbox"
                        className="switch"
                        checked={user.active}
                        onChange={() => onToggleStatus(user)}
                      />
                      <span
                        className={
                          user.active ? "status-active" : "status-inactive"
                        }
                      >
                        {user.active ? "Activo" : "Inactivo"}
                      </span>
                    </div>
                  </TableCell>

                  <TableCell className="text-right">
                    <button
                      className="btn-outline btn-sm"
                      onClick={() => handleEditUser(user)}
                    >
                      <Edit size={16} className="mr-1" />
                      Editar
                    </button>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>

        {/* === Paginación === */}
        {totalPages > 1 && (
          <div className="pagination">
            <button
              disabled={currentPage === 1}
              onClick={() => setCurrentPage((prev) => Math.max(1, prev - 1))}
            >
              Anterior
            </button>
            {Array.from({ length: totalPages }, (_, i) => (
              <button
                key={i}
                className={currentPage === i + 1 ? "active" : ""}
                onClick={() => setCurrentPage(i + 1)}
              >
                {i + 1}
              </button>
            ))}
            <button
              disabled={currentPage === totalPages}
              onClick={() =>
                setCurrentPage((prev) => Math.min(totalPages, prev + 1))
              }
            >
              Siguiente
            </button>
          </div>
        )}
      </div>
</div>

      {/* === Modal de edición con formulario (usa mismos componentes/clases que el form de registro) === */}
      {isEditDialogOpen && editForm && (
        <div
          className="modal-overlay"
          onMouseDown={handleCloseEditDialog}
          role="presentation"
        >
          <div
            onMouseDown={(e) => e.stopPropagation()}
            aria-modal="true"
            role="dialog"
            style={{ maxWidth: 720 }}
          >
            <Card className="card">
              <CardHeader className="card-header">
                <CardTitle className="card-title">Editar Usuario</CardTitle>
              </CardHeader>
              <CardContent>
                <form className="space-y-4" onSubmit={handleSave} noValidate>
                  <div className="form-grid">
                    <div>
                      <Label htmlFor="edit-name">Nombre</Label>
                      <Input
                        id="edit-name"
                        type="text"
                        value={editForm.name}
                        onChange={(e) => handleEditChange("name", e.target.value)}
                        placeholder="Nombre"
                        required
                      />
                      {editErrors.name && <div className="error-message">{editErrors.name}</div>}
                    </div>

                    <div>
                      <Label htmlFor="edit-lastname">Apellido</Label>
                      <Input
                        id="edit-lastname"
                        type="text"
                        value={editForm.lastname}
                        onChange={(e) => handleEditChange("lastname", e.target.value)}
                        placeholder="Apellido"
                        required
                      />
                      {editErrors.lastname && <div className="error-message">{editErrors.lastname}</div>}
                    </div>
                  </div>

                  <div className="div-edit-email">
                    <Label htmlFor="edit-email">Correo Electrónico</Label>
                    <Input
                      id="edit-email"
                      type="email"
                      value={editForm.email}
                      onChange={(e) => handleEditChange("email", e.target.value)}
                      placeholder="ejemplo@correo.com"
                      required
                    />
                    {editErrors.email && <div className="error-message">{editErrors.email}</div>}
                  </div>

                  {/* No password field per request - password managed elsewhere */}

                  <div>
                    <Label htmlFor="edit-role">Rol</Label>
                    <Select
                      value={editForm.role}
                      onValueChange={(value) => handleEditChange("role", value)}
                    >
                      <SelectTrigger>
                        <SelectValue placeholder="Seleccione un rol" />
                      </SelectTrigger>
                      <SelectContent className="select-content">
                        <SelectItem value="DUENO">Dueño</SelectItem>
                        <SelectItem value="ENCARGADO">Encargado</SelectItem>
                        <SelectItem value="ATENCION">Atención</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>

                  {editErrors.form && <div className="error-message">{editErrors.form}</div>}

                  <div style={{ display: "flex", gap: 8 }}>
                    <Button
                      type="button"
                      variant="outline"
                      size="sm"
                      onClick={handleCloseEditDialog}
                      disabled={isSaving}
                      className="w-full"
                    >
                      Cancelar
                    </Button>

                    <Button
                      type="submit"
                      variant="default"
                      size="sm"
                      disabled={isSaving}
                      className="w-full"
                      onClick={handleSave} /* asegurar ejecución aunque Button no propague submit */
                    >
                      {isSaving ? "Guardando..." : "Guardar Cambios"}
                    </Button>
                  </div>
                </form>
              </CardContent>
            </Card>
          </div>
        </div>
      )}
    </div>
  );
}
