import { useState } from 'react';
import { createOrder } from '../services/orderService';

export const useRegisterOrder = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const registerOrder = async (orderData) => {
        setLoading(true);
        setError(null);
        
        try {
            const response = await createOrder(orderData);
            setLoading(false);
            return response;
        } catch (err) {
            setError(err.message || 'Error al registrar la orden');
            setLoading(false);
            throw err;
        }
    };

    return {
        registerOrder,
        loading,
        error
    };
};