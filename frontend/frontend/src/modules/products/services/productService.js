// src/modules/product/services/productService.js

/**
 * Servicio para manejar las operaciones CRUD de productos.
 */
import apiClient from "../../../shared/services/apiClient";

// --- Endpoints por defecto ---
const DEFAULT_CREATE_ENDPOINT = "/api/products";
const DEFAULT_GETALL_ENDPOINT = "/api/products";
const DEFAULT_UPDATE_ENDPOINT = "/api/products/<productId>";
const DEFAULT_DELETE_ENDPOINT = "/api/products/<productId>";
const DEFAULT_TOGGLE_STATUS_ENDPOINT = "/api/products/<productId>/status";

/**
 * Crear un nuevo producto en el backend.
 * @param {Object} payload - Datos del producto ({ name, description, price, stock, categoryId, active, imageUrl }).
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor.
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const create = async (payload, { endpoint = DEFAULT_CREATE_ENDPOINT } = {}) => {
  try {
    const response = await apiClient.post(endpoint, payload);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || null;
    const message = serverMessage || error.message || "Failed to create product";
    throw new Error(message);
  }
};

/**
 * Obtener el listado completo de productos desde el backend.
 * @returns {Promise<Array>} - Array de productos.
 * @throws {Error} - Re-lanza el error si falla la petición.
 */
const list = async () => {
  try {
    const { data } = await apiClient.get(DEFAULT_GETALL_ENDPOINT);
    return data;
  } catch (err) {
    console.error("Error fetching products:", err);
    throw err;
  }
};

/**
 * Actualizar un producto en el backend.
 * @param {string|number} productId - ID del producto a actualizar.
 * @param {Object} payload - Datos a actualizar.
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor.
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const update = async (productId, payload, { endpoint = DEFAULT_UPDATE_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<productId>", productId);
    const response = await apiClient.put(url, payload);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || error.message;
    throw new Error(serverMessage || "Failed to update product");
  }
};

/**
 * Eliminar un producto del backend.
 * @param {string|number} productId - ID del producto a eliminar.
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor.
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const deleteProduct = async (productId, { endpoint = DEFAULT_DELETE_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<productId>", productId);
    const response = await apiClient.delete(url);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || error.message;
    throw new Error(serverMessage || "Failed to delete product");
  }
};

/**
 * Activar o desactivar un producto mediante PATCH.
 * @param {string|number} productId - ID del producto.
 * @param {Object} payload - Nuevo estado del producto ({ active: true/false }).
 * @param {Object} options - Opciones (p. ej. endpoint personalizado).
 * @returns {Promise<Object>} - Respuesta del servidor.
 * @throws {Error} - Mensaje normalizado en caso de fallo.
 */
const toggleStatus = async (productId, payload, { endpoint = DEFAULT_TOGGLE_STATUS_ENDPOINT } = {}) => {
  try {
    const url = endpoint.replace("<productId>", productId);
    const response = await apiClient.patch(url, payload);
    return response.data;
  } catch (error) {
    const serverMessage = error.response?.data?.message || error.response?.data || error.message;
    throw new Error(serverMessage || "Failed to toggle product status");
  }
};

export {
  create,
  list,
  update,
  deleteProduct,
  toggleStatus
};