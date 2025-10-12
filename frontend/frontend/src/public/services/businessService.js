import apiClient from "../../shared/services/apiClient"; // Asegúrate de que esta ruta sea correcta


const DEFAULT_GETBUSINESSDATA_ENDPOINT = "/api/public/getBusinessData";



/**
 * Obtener el listado completo de horarios desde el backend del enpoint publico
 * @returns {Promise<Array>} - Array de horarios activos.
 * @throws {Error} - Re-lanza el error si falla la petición.
 */
const getBusinessData = async () => {
  try {
    const { data } = await apiClient.get(DEFAULT_GETBUSINESSDATA_ENDPOINT);
    return data;
  } catch (err) {
    console.error("Error fetching business data:", err);
    throw err;
  }
};


// Exportamos todas las funciones de forma nombrada
export {
 getBusinessData
};