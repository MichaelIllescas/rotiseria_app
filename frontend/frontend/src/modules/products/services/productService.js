/**
 * Servicio para manejar las operaciones CRUD de productos.
 */
import apiClient from "../../../shared/services/apiClient";

const PRODUCT_BASE_URL = "/api/products";
const DEFAULT_CREATE_ENDPOINT = `${PRODUCT_BASE_URL}/create`;
const DEFAULT_GETALL_ENDPOINT = `${PRODUCT_BASE_URL}/getAll`;
const DEFAULT_UPDATE_ENDPOINT = `${PRODUCT_BASE_URL}/update/<productId>`;
const DEFAULT_DELETE_ENDPOINT = `${PRODUCT_BASE_URL}/delete/<productId>`;
const DEFAULT_TOGGLE_STATUS_ENDPOINT = `${PRODUCT_BASE_URL}/<productId>/status`;

/**
 * 🖼️ Subir imagen de producto y obtener la URL pública.
 * @param {File} file - Archivo de imagen a subir.
 * @returns {Promise<string>} - URL pública de la imagen.
 */
const uploadImage = async (file) => {
  const formData = new FormData();
  formData.append("image", file);

  try {
    const { data } = await apiClient.post(`${PRODUCT_BASE_URL}/upload`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    return data.imageUrl;
  } catch (error) {
    const msg =
      error.response?.data?.message ||
      error.response?.data ||
      error.message ||
      "Error al subir la imagen";
    throw new Error(msg);
  }
};

/**
 * 🟢 Crear un nuevo producto (ya con imageUrl generado).
 * @param {Object} payload - Datos del producto.
 * @param {Object} options - Opciones personalizadas.
 * @returns {Promise<Object>} - Respuesta del backend.
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
 * 📋 Obtener todos los productos.
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
 * ✏️ Actualizar producto existente.
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
 * 🗑️ Eliminar producto.
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
 * 🔄 Cambiar estado (activar/desactivar).
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
  uploadImage,
  create,
  list,
  update,
  deleteProduct as remove,
  toggleStatus,
};
