import apiClient from "../../shared/services/apiClient"; // Asegúrate de que esta ruta sea correcta


const DEFAULT_GETBUSINESSHOURS_ENDPOINT = "/api/public/getBusinessHours";



/**
 * Obtener el listado completo de horarios desde el backend del enpoint publico
 * @returns {Promise<Array>} - Array de horarios activos.
 * @throws {Error} - Re-lanza el error si falla la petición.
 */
const listBusinessHours = async () => {
  try {
    const { data } = await apiClient.get(DEFAULT_GETBUSINESSHOURS_ENDPOINT);
    return data;
  } catch (err) {
    console.error("Error fetching business hours:", err);
    throw err;
  }
};


// Exportamos todas las funciones de forma nombrada
export {
 listBusinessHours
};