import { useState } from "react";
import { update as updateUserService } from "../services/userService";
import { toast } from "../../../ui/toaster";

/**
 * Hook para actualizar un usuario.
 * Encapsula la llamada al servicio `update` y maneja:
 *  - estado de carga (isUpdating)
 *  - errores (error)
 *  - notificaciones (toast.success / toast.error)
 *
 * @returns {{ updateUser: Function, isUpdating: boolean, error: any }}
 */
export default function useUpdateUser() {
  const [isUpdating, setIsUpdating] = useState(false);
  const [error, setError] = useState(null);

  /**
   * Ejecuta la petición de actualización al backend.
   * Muestra un toast de éxito o error según corresponda.
   * @param {Object} payload - Debe incluir al menos `id` y los campos a actualizar.
   * @returns {Promise<Object>} - Resultado devuelto por el servicio (o lanza error).
   */
  const updateUser = async (payload) => {
    setIsUpdating(true);
    setError(null);
    try {
      // payload debe incluir id
      const result = await updateUserService(payload.id, payload);
      // mostrar confirmación reutilizando el Toaster existente
      toast.success(result?.message || "Usuario actualizado correctamente");
      return result;
    } catch (err) {
      const message = err?.message || "Error al actualizar usuario";
      toast.error(message);
      setError(err);
      throw err;
    } finally {
      setIsUpdating(false);
    }
  };

  return { updateUser, isUpdating, error };
}
