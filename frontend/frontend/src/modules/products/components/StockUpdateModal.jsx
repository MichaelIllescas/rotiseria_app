import React from 'react';
import { Check } from 'lucide-react';

const StockUpdateModal = ({ 
  isOpen, 
  onClose, 
  stockData, 
  onStockChange, 
  onSave, 
  isLoading, 
  hasChanges 
}) => {
  if (!isOpen) return null;

  const handleStockChange = (productId, value) => {
    const numValue = parseInt(value);
    if (isNaN(numValue) || numValue < 0) return;
    onStockChange(productId, value);
  };

  const changedCount = stockData.filter(item => item.hasChanged).length;

  return (
    <div className="stock-modal-overlay" onClick={onClose}>
      <div className="stock-modal-container" onClick={e => e.stopPropagation()}>
        <div className="stock-modal-header">
          <h2 className="stock-modal-title">Actualizar Stock</h2>
          <button 
            className="stock-modal-close" 
            onClick={onClose}
            disabled={isLoading}
          >
            ×
          </button>
        </div>

        <div className="stock-modal-body">
          <div className="stock-list">
            {stockData.map((item) => (
              <div 
                key={item.productId} 
                className={`stock-item ${item.hasChanged ? 'changed' : ''}`}
              >
                <div className="stock-product-info">
                  <div className="stock-product-name">{item.name}</div>
                  <div className="stock-current">Stock actual: {item.currentStock}</div>
                </div>
                
                <div className="stock-input-container">
                  <input
                    type="number"
                    min="0"
                    value={item.newStock}
                    onChange={(e) => handleStockChange(item.productId, e.target.value)}
                    className={`stock-input-field ${item.hasChanged ? 'changed' : ''}`}
                    disabled={isLoading}
                  />
                  <div className={`stock-change-indicator ${item.hasChanged ? 'visible' : ''}`} />
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="stock-modal-footer">
          <div className={`stock-changes-info ${hasChanges ? 'has-changes' : ''}`}>
            {hasChanges 
              ? `${changedCount} producto${changedCount !== 1 ? 's' : ''} modificado${changedCount !== 1 ? 's' : ''}`
              : 'Sin cambios'
            }
          </div>
          
          <div className="stock-modal-actions">
            <button 
              className="stock-cancel-btn" 
              onClick={onClose}
              disabled={isLoading}
            >
              Cancelar
            </button>
            <button 
              className="stock-save-btn" 
              onClick={onSave}
              disabled={!hasChanges || isLoading}
            >
              {isLoading ? (
                <>
                  <div className="reload-icon" style={{ animation: 'reload-rotate 1s linear infinite' }}>⟳</div>
                  Guardando...
                </>
              ) : (
                <>
                  <Check size={16} />
                  Confirmar Cambios
                </>
              )}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default StockUpdateModal;
