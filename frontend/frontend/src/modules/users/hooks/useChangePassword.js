import { useState, useCallback } from "react";
import {changePassword as changeUserPassword} from "../services/userService";
import { toast, Toaster } from "../../../ui/toaster";
/**
 * useChangePassword()
 * - Qué hace: expone una función `changePassword(id, newPassword)` que llama al service.
 *   Proporciona estados `loading` y `error` para controlar la UI.
 * - Qué no hace: no actualiza el contexto de usuario ni hace redirecciones; el componente consumidor
 *   debe encargarse de esas acciones si son necesarias.
 */
export default function useChangePassword() {
	const [loading, setLoading] = useState(false);
	const [error, setError] = useState(null);

	const changePassword = useCallback(async (id, newPassword) => {
		setLoading(true);
		setError(null);
		try {
			const result = await changeUserPassword(id, newPassword);
			setLoading(false);
            toast.success("Clave actualizada con éxito");
			return result;
		} catch (err) {
			setError(err);
			setLoading(false);
			// Re-lanzar para que el llamador pueda manejar la excepción si lo desea
			throw err;
		}
	}, []);

	return {
		changePassword,
		loading,
		error,
		setError, // expuesto por si el componente quiere limpiar el error manualmente
	};
}
