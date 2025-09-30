/**
 * Servicio para manejar usuarios (registro, etc.)
 */
import apiClient from "../../../shared/services/apiClient";

const DEFAULT_REGISTER_ENDPOINT = "/api/users/register"; 

/**
 * Registrar un nuevo usuario en el backend.
 * @param {Object} payload - Datos del usuario ({ name, lastname, email, password, ... }).
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor (response.data).
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const register = async (payload, { endpoint = DEFAULT_REGISTER_ENDPOINT } = {}) => {
  try {
    // payload ejemplo: { name, lastname, email, password, ... }
    const response = await apiClient.post(endpoint, payload);
    return response.data;
  } catch (error) {
    // Normalizar error para el frontend
    const serverMessage = error.response?.data?.message || error.response?.data || null;
    const message = serverMessage || error.message || "Registration failed";
    // lanzar Error para que el componente que llame lo capture
    throw new Error(message);
  }
};


const DEFAULT_GETALL_ENDPOINT = "/api/users/getAll"; 

/**
 * Obtener el listado completo de usuarios desde el backend.
 * @returns {Promise<Array>} - Array de usuarios.
 * @throws {Error} - Re-lanza el error si falla la petición.
 */
const list = async () => {
  try {
    const { data } = await apiClient.get(DEFAULT_GETALL_ENDPOINT);
    return data;
  } catch (err) {
    console.error("Error fetching users:", err);
    throw err;
  }
};


const DEFAULT_ACTIVE_ENDPOINT = "/api/users/activate/<userId>"; 
/**
 * Activar (toggle) un usuario mediante PATCH a un endpoint específico.
 * @param {string|number} userId - ID del usuario a activar.
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor.
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const toggleActive = async (userId, { endpoint = DEFAULT_ACTIVE_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<userId>", userId);
    const response = await apiClient.patch(url);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || error.message;
    throw new Error(serverMessage || "Failed to toggle user status");
  }
};


const DEFAULT_DESACTIVE_ENDPOINT = "/api/users/desactivate/<userId>"; 
/**
 * Desactivar (toggle) un usuario mediante PATCH a un endpoint específico.
 * @param {string|number} userId - ID del usuario a desactivar.
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor.
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const toggleDesactive = async (userId, { endpoint = DEFAULT_DESACTIVE_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<userId>", userId);
    const response = await apiClient.patch(url);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || error.message;
    throw new Error(serverMessage || "Failed to toggle user status");
  }
};


/* === Nuevo: update user === */
const DEFAULT_UPDATE_ENDPOINT = "/api/users/update/<userId>";

/**
 * Actualizar un usuario en el backend.
 * Realiza una petición PUT a /users/update/{id} con el payload proporcionado.
 * @param {string|number} userId - ID del usuario a actualizar.
 * @param {Object} payload - Datos a actualizar ({ name, lastname, email, role, active, ... }).
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor (normalmente el usuario actualizado o mensaje).
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const update = async (userId, payload, { endpoint = DEFAULT_UPDATE_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<userId>", userId);
    const response = await apiClient.put(url, payload);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || error.message;
    throw new Error(serverMessage || "Failed to update user");
  }
};

/* === Nuevo: changePassword === */
const DEFAULT_CHANGEPASSWORD_ENDPOINT = "/api/users/changePassword/<userId>";

/**
 * Cambiar la contraseña de un usuario en el backend.
 * Realiza una petición POST a /users/changePassword/{id} con el payload proporcionado.
 * @param {string|number} userId - ID del usuario cuya contraseña se va a cambiar.
 * @param {Object} payload - Datos a actualizar ({ newPassword }).
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor (normalmente el usuario actualizado o mensaje).
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const changePassword = async (userId, payload, { endpoint = DEFAULT_CHANGEPASSWORD_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<userId>", userId);
    const response = await apiClient.patch(url, payload);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || error.message;
    throw new Error(serverMessage || "Failed to change password");
  }
};


export {
  register,
  list,
  toggleActive,
  toggleDesactive,
  update,
  changePassword
};
