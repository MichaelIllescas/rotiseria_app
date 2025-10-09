import { useState, useCallback } from 'react';
import { updateBulkStock } from '../modules/products/services/productService';

export const useStockUpdate = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [stockData, setStockData] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [hasChanges, setHasChanges] = useState(false);

  const openModal = useCallback((products) => {
    const initialStockData = products.map(product => ({
      productId: product.id,
      name: product.name,
      currentStock: product.dailyStock,
      newStock: product.dailyStock,
      hasChanged: false
    }));
    
    setStockData(initialStockData);
    setIsModalOpen(true);
    setHasChanges(false);
  }, []);

  const closeModal = useCallback(() => {
    setIsModalOpen(false);
    setStockData([]);
    setHasChanges(false);
  }, []);

  const updateStock = useCallback((productId, newStock) => {
    setStockData(prevData => {
      const updatedData = prevData.map(item => {
        if (item.productId === productId) {
          const stockValue = parseInt(newStock) || 0;
          const hasChanged = stockValue !== item.currentStock;
          return {
            ...item,
            newStock: stockValue,
            hasChanged
          };
        }
        return item;
      });

      const anyChanges = updatedData.some(item => item.hasChanged);
      setHasChanges(anyChanges);

      return updatedData;
    });
  }, []);

  const submitStockUpdates = useCallback(async () => {
    setIsLoading(true);
    
    try {
      const changedProducts = stockData
        .filter(item => item.hasChanged)
        .map(item => ({
          productId: item.productId,
          stock: item.newStock
        }));

      if (changedProducts.length === 0) {
        throw new Error('No hay cambios para actualizar');
      }

      await updateBulkStock(changedProducts);
      return { success: true, updatedCount: changedProducts.length };
    } catch (error) {
      console.error('Error al actualizar stock:', error);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [stockData]);

  return {
    isModalOpen,
    stockData,
    isLoading,
    hasChanges,
    openModal,
    closeModal,
    updateStock,
    submitStockUpdates
  };
};
