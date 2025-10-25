import apiClient from "../../shared/services/apiClient";

const DEFAULT_CREATE_ORDER_ENDPOINT = "/api/public/createOrder";
const DEFAULT_GETALL_ORDERS_ENDPOINT = "/api/orders/getAll";

/**
 * Crear una nueva orden
 * @param {Object} orderData - Datos de la orden a crear
 * @returns {Promise<Object>} - Orden creada
 * @throws {Error} - Re-lanza el error si falla la petición
 */
const createOrder = async (orderData) => {
    try {
        const { data } = await apiClient.post(DEFAULT_CREATE_ORDER_ENDPOINT, orderData);
        return data;
    } catch (err) {
        console.error("Error creating order:", err);
        throw err;
    }
};

/**
 * Obtener todas las órdenes
 * @returns {Promise<Array>} - Array de órdenes
 * @throws {Error} - Re-lanza el error si falla la petición
 */
const getAllOrders = async () => {
    try {
        const { data } = await apiClient.get(DEFAULT_GETALL_ORDERS_ENDPOINT);
        return data;
    } catch (err) {
        console.error("Error fetching orders:", err);
        throw err;
    }
};

// Exportamos todas las funciones de forma nombrada
export {
    createOrder,
    getAllOrders
};
