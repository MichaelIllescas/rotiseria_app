import apiClient from "../../../shared/services/apiClient"; // Asegúrate de que esta ruta sea correcta


const DEFAULT_GETALL_ACTIVES_ENDPOINT = "/api/categories/active";



/**
 * Obtener el listado completo de categorías desde el backend del enpoint publico
 * @returns {Promise<Array>} - Array de categorías activas.
 * @throws {Error} - Re-lanza el error si falla la petición.
 */
const listActivesCategories = async () => {
  try {
    const { data } = await apiClient.get(DEFAULT_GETALL_ACTIVES_ENDPOINT);
    return data;
  } catch (err) {
    console.error("Error fetching categories:", err);
    throw err;
  }
};


// Exportamos todas las funciones de forma nombrada
export {
 listActivesCategories
};