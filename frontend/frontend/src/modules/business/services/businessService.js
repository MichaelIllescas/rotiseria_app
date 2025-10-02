/**
 * Servicio para manejar las operaciones CRUD de negocios.
 */
import apiClient from "../../../shared/services/apiClient";

// --- Endpoints por defecto ---
const DEFAULT_CREATE_ENDPOINT = "/api/business/create";
const DEFAULT_GETALL_ENDPOINT = "/api/business/getAll";
const DEFAULT_GETBYID_ENDPOINT = "/api/business/<businessId>";
const DEFAULT_UPDATE_ENDPOINT = "/api/business/update/<businessId>";
const DEFAULT_DELETE_ENDPOINT = "/api/business/delete/<businessId>";
const DEFAULT_TOGGLE_STATUS_ENDPOINT = "/api/business/toggleStatus/<businessId>";

/**
 * Crear un nuevo negocio en el backend.
 * @param {Object} payload - Datos del negocio ({ name, description, address, phone, email, active }).
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
    const message = serverMessage || error.message || "Failed to create business";
    throw new Error(message);
  }
};

/**
 * Obtener el listado completo de negocios desde el backend.
 * @returns {Promise<Array>} - Array de negocios.
 * @throws {Error} - Re-lanza el error si falla la petición.
 */
const list = async () => {
  try {
    const { data } = await apiClient.get(DEFAULT_GETALL_ENDPOINT);
    return data;
  } catch (err) {
    console.error("Error fetching businesses:", err);
    throw err;
  }
};

/**
 * Obtener un negocio específico por ID desde el backend.
 * @param {string|number} businessId - ID del negocio a obtener.
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Datos del negocio.
 * @throws {Error} - Re-lanza el error si falla la petición.
 */
const getById = async (businessId, { endpoint = DEFAULT_GETBYID_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<businessId>", businessId);
    const { data } = await apiClient.get(url);
    return data;
  } catch (err) {
    console.error(`Error fetching business with ID ${businessId}:`, err);
    throw err;
  }
};

/**
 * Actualizar un negocio en el backend.
 * Realiza una petición PUT al endpoint del negocio con el payload proporcionado.
 * @param {string|number} businessId - ID del negocio a actualizar.
 * @param {Object} payload - Datos a actualizar ({ name, description, address, phone, email, active }).
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor (normalmente el negocio actualizado).
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const update = async (businessId, payload, { endpoint = DEFAULT_UPDATE_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<businessId>", businessId);
    const response = await apiClient.put(url, payload);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || error.message;
    throw new Error(serverMessage || "Failed to update business");
  }
};

/**
 * Eliminar un negocio del backend.
 * Realiza una petición DELETE al endpoint del negocio.
 * @param {string|number} businessId - ID del negocio a eliminar.
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor.
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const deleteBusiness = async (businessId, { endpoint = DEFAULT_DELETE_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<businessId>", businessId);
    const response = await apiClient.delete(url);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || error.message;
    throw new Error(serverMessage || "Failed to delete business");
  }
};

/**
 * Activar o desactivar un negocio mediante PATCH.
 * @param {string|number} businessId - ID del negocio.
 * @param {Object} payload - Nuevo estado del negocio ({ active: true/false }).
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor.
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const toggleStatus = async (businessId, payload, { endpoint = DEFAULT_TOGGLE_STATUS_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<businessId>", businessId);
    const response = await apiClient.patch(url, payload);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || error.message;
    throw new Error(serverMessage || "Failed to toggle business status");
  }
};

// Exportamos todas las funciones de forma nombrada
export {
  create,
  list,
  getById,
  update,
  deleteBusiness,
  toggleStatus
};