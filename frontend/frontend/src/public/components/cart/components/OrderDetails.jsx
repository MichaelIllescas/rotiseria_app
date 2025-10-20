import React from 'react';
import { 
  ArrowLeft, CheckCircle, Clock, CreditCard, HandCoins, 
  MapPin, Store, User, Phone, FileText, Package, 
  Calendar, Hash, ChevronRight
} from 'lucide-react';
import '../../../styles/OrderDetails.css';

export const OrderDetails = ({ 
  orderData,
  onBack,
  onReorder,
  onContactSupport
}) => {

  const getPaymentMethodInfo = (method) => {
    switch (method) {
      case 'mercadopago':
        return {
          icon: CreditCard,
          label: 'Mercado Pago',
          status: orderData.paymentStatus === 'approved' 
            ? 'Pago confirmado' 
            : 'Procesando pago',
          color: orderData.paymentStatus === 'approved' ? 'success' : 'warning'
        };
      case 'cash':
        return {
          icon: HandCoins,
          label: 'Pago en efectivo',
          status: 'Pendiente de pago',
          color: 'warning'
        };
      default:
        return {
          icon: Clock,
          label: 'Método no especificado',
          status: 'Pendiente',
          color: 'secondary'
        };
    }
  };

  const getOrderStatusInfo = (status) => {
    switch (status) {
      case 'confirmed':
        return {
          color: 'success',
          text: 'Confirmado',
          description: 'Tu pedido ha sido confirmado y está siendo preparado',
          icon: CheckCircle
        };
      case 'preparing':
        return {
          color: 'warning',
          text: 'En preparación',
          description: 'Estamos preparando tu pedido',
          icon: Clock
        };
      case 'ready':
        return {
          color: 'info',
          text: 'Listo',
          description: 'Tu pedido está listo para entregar/retirar',
          icon: Package
        };
      case 'delivered':
        return {
          color: 'success',
          text: 'Entregado',
          description: 'Tu pedido ha sido entregado exitosamente',
          icon: CheckCircle
        };
      case 'pending':
        return {
          color: 'warning',
          text: 'Pendiente',
          description: 'Tu pedido está pendiente de confirmación',
          icon: Clock
        };
      default:
        return {
          color: 'secondary',
          text: 'Estado desconocido',
          description: 'Contacta con el establecimiento para más información',
          icon: Clock
        };
    }
  };

  const paymentInfo = getPaymentMethodInfo(orderData.paymentMethod);
  const statusInfo = getOrderStatusInfo(orderData.status);
  const PaymentIcon = paymentInfo.icon;
  const StatusIcon = statusInfo.icon;

  const customer = orderData.customer || {};
  const products = orderData.products || [];

  // Calcular totales
  const subtotal = products.reduce((sum, product) => sum + (product.price * product.quantity), 0);
  const shippingCost = orderData.shippingCost || 0;
  const total = subtotal + shippingCost;

  return (
    <div className="cart-container">
      {/* Header Banner */}
      <div className="cart-header cart-header-banner">
        <div className="order-icon-banner">
          <StatusIcon size={48} />
        </div>
        <h1>📋 Detalle del Pedido</h1>
        <p className="cart-subtitle">
          Pedido #{orderData.orderNumber} - {statusInfo.text}
        </p>
        <div className="order-date">
          <Calendar size={16} />
          <span>{orderData.createdAt || new Date().toLocaleDateString()}</span>
        </div>
      </div>

      {/* Contenido principal */}
      <div className="cart-content">
        <section className="order-details-section">
          
          {/* Estado actual del pedido */}
          <div className={`order-status-card ${statusInfo.color}`}>
            <div className="status-header">
              <StatusIcon size={24} />
              <div className="status-info">
                <h3 className="status-title">{statusInfo.text}</h3>
                <p className="status-description">{statusInfo.description}</p>
              </div>
            </div>
            {orderData.estimatedTime && (
              <div className="estimated-time">
                <Clock size={16} />
                <span>Tiempo estimado: {orderData.estimatedTime}</span>
              </div>
            )}
          </div>

          {/* Productos ordenados */}
          <div className="products-card">
            <h3 className="card-title">
              <Package size={20} />
              Productos ordenados
            </h3>
            <div className="products-list">
              {products.map((product, index) => (
                <div key={index} className="product-item">
                  <div className="product-info">
                    <h4 className="product-name">{product.name}</h4>
                    <p className="product-description">{product.description}</p>
                    {product.modifications && product.modifications.length > 0 && (
                      <div className="product-modifications">
                        <span className="modifications-label">Modificaciones:</span>
                        <ul>
                          {product.modifications.map((mod, idx) => (
                            <li key={idx}>{mod}</li>
                          ))}
                        </ul>
                      </div>
                    )}
                  </div>
                  <div className="product-quantity">
                    <span className="quantity">×{product.quantity}</span>
                  </div>
                  <div className="product-price">
                    <span className="unit-price">${product.price.toLocaleString()}</span>
                    <span className="total-price">
                      ${(product.price * product.quantity).toLocaleString()}
                    </span>
                  </div>
                </div>
              ))}
            </div>
            
            {/* Totales */}
            <div className="order-totals">
              <div className="total-row">
                <span>Subtotal:</span>
                <span>${subtotal.toLocaleString()}</span>
              </div>
              {shippingCost > 0 && (
                <div className="total-row">
                  <span>Envío:</span>
                  <span>${shippingCost.toLocaleString()}</span>
                </div>
              )}
              <div className="total-row final-total">
                <span>Total:</span>
                <span>${total.toLocaleString()}</span>
              </div>
            </div>
          </div>

          {/* Información de entrega/retiro */}
          <div className="delivery-card">
            <h3 className="card-title">
              {orderData.shippingType === 'delivery' ? <MapPin size={20} /> : <Store size={20} />}
              {orderData.shippingType === 'delivery' ? 'Información de entrega' : 'Información de retiro'}
            </h3>
            <div className="delivery-info">
              {orderData.shippingType === 'delivery' ? (
                <div className="address-info">
                  <div className="address-line">
                    <strong>Dirección:</strong>
                    <span>{orderData.address?.street} {orderData.address?.number}</span>
                  </div>
                  {orderData.address?.details && (
                    <div className="address-line">
                      <strong>Detalles:</strong>
                      <span>{orderData.address.details}</span>
                    </div>
                  )}
                  {orderData.address?.reference && (
                    <div className="address-line">
                      <strong>Referencia:</strong>
                      <span>{orderData.address.reference}</span>
                    </div>
                  )}
                </div>
              ) : (
                <div className="pickup-info">
                  <p><strong>Retiro en local</strong></p>
                  <p>Dirección del local: [Dirección del establecimiento]</p>
                  <p>Horarios de atención: [Horarios]</p>
                </div>
              )}
            </div>
          </div>

          {/* Información del cliente */}
          <div className="customer-card">
            <h3 className="card-title">
              <User size={20} />
              Información del cliente
            </h3>
            <div className="customer-info">
              <div className="info-row">
                <User size={16} />
                <span className="info-label">Nombre:</span>
                <span className="info-value">{customer.name || 'No especificado'}</span>
              </div>
              {customer.document && (
                <div className="info-row">
                  <FileText size={16} />
                  <span className="info-label">Documento:</span>
                  <span className="info-value">{customer.document}</span>
                </div>
              )}
              {customer.phone && (
                <div className="info-row">
                  <Phone size={16} />
                  <span className="info-label">Teléfono:</span>
                  <span className="info-value">{customer.phone}</span>
                </div>
              )}
              {customer.observations && (
                <div className="info-row observations">
                  <FileText size={16} />
                  <span className="info-label">Observaciones:</span>
                  <span className="info-value">{customer.observations}</span>
                </div>
              )}
            </div>
          </div>

          {/* Información de pago */}
          <div className="payment-card">
            <h3 className="card-title">
              <PaymentIcon size={20} />
              Información de pago
            </h3>
            <div className="payment-info">
              <div className="payment-method">
                <span className="method-label">Método de pago:</span>
                <span className="method-value">{paymentInfo.label}</span>
              </div>
              <div className={`payment-status ${paymentInfo.color}`}>
                <span className="status-label">Estado:</span>
                <span className="status-value">{paymentInfo.status}</span>
              </div>
              <div className="payment-total">
                <span className="total-label">Total:</span>
                <span className="total-value">${total.toLocaleString()}</span>
              </div>
            </div>
          </div>

          {/* Botones de acción */}
          <div className="order-actions">
            <button onClick={onBack} className="secondary-button">
              <ArrowLeft size={16} />
              Volver
            </button>
            
            <div className="action-buttons">
              {onReorder && (
                <button onClick={onReorder} className="tertiary-button">
                  <Package size={16} />
                  Reordenar
                </button>
              )}
              
              {onContactSupport && (
                <button onClick={onContactSupport} className="support-button">
                  <Phone size={16} />
                  Contactar soporte
                </button>
              )}
            </div>
          </div>

        </section>
      </div>
    </div>
  );
};