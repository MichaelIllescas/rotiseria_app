/**
 * Servicio para manejar las operaciones CRUD de categorías.
 */
import apiClient from "../../../shared/services/apiClient"; // Asegúrate de que esta ruta sea correcta

// --- Endpoints por defecto ---
const DEFAULT_CREATE_ENDPOINT = "/api/categories";
const DEFAULT_GETALL_ENDPOINT = "/api/categories";
const DEFAULT_UPDATE_ENDPOINT = "/api/categories/<categoryId>";
const DEFAULT_DELETE_ENDPOINT = "/api/categories/<categoryId>";
const DEFAULT_TOGGLE_STATUS_ENDPOINT = "/api/categories/<categoryId>/status";

/**
 * Crear una nueva categoría en el backend.
 * @param {Object} payload - Datos de la categoría ({ name, description, active }).
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor (response.data).
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const create = async (payload, { endpoint = DEFAULT_CREATE_ENDPOINT } = {}) => {
  try {
    const response = await apiClient.post(endpoint, payload);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || null;
    const message = serverMessage || error.message || "Failed to create category";
    throw new Error(message);
  }
};

/**
 * Obtener el listado completo de categorías desde el backend.
 * @returns {Promise<Array>} - Array de categorías.
 * @throws {Error} - Re-lanza el error si falla la petición.
 */
const list = async () => {
  try {
    const { data } = await apiClient.get(DEFAULT_GETALL_ENDPOINT);
    return data;
  } catch (err) {
    console.error("Error fetching categories:", err);
    throw err;
  }
};

/**
 * Actualizar una categoría en el backend.
 * Realiza una petición PUT al endpoint de la categoría con el payload proporcionado.
 * @param {string|number} categoryId - ID de la categoría a actualizar.
 * @param {Object} payload - Datos a actualizar ({ name, description, active }).
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor (normalmente la categoría actualizada).
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const update = async (categoryId, payload, { endpoint = DEFAULT_UPDATE_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<categoryId>", categoryId);
    const response = await apiClient.put(url, payload);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || error.message;
    throw new Error(serverMessage || "Failed to update category");
  }
};

/**
 * Eliminar una categoría del backend.
 * Realiza una petición DELETE al endpoint de la categoría.
 * @param {string|number} categoryId - ID de la categoría a eliminar.
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor.
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const deleteCategory = async (categoryId, { endpoint = DEFAULT_DELETE_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<categoryId>", categoryId);
    const response = await apiClient.delete(url);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || error.message;
    throw new Error(serverMessage || "Failed to delete category");
  }
};

/**
 * Activar o desactivar una categoría mediante PATCH.
 * @param {string|number} categoryId - ID de la categoría.
 * @param {Object} payload - Nuevo estado de la categoría ({ active: true/false }).
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor.
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const toggleStatus = async (categoryId, payload, { endpoint = DEFAULT_TOGGLE_STATUS_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<categoryId>", categoryId);
    const response = await apiClient.patch(url, payload);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || error.message;
    throw new Error(serverMessage || "Failed to toggle category status");
  }
};

// Exportamos todas las funciones de forma nombrada
export {
  create,
  list,
  update,
  deleteCategory,
  toggleStatus
};