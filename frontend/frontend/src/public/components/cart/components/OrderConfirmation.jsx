import React from "react";
import {
  CheckCircle,
  Clock,
  CreditCard,
  HandCoins,
  MapPin,
  Store,
  ArrowLeft,
  User,
  Phone,
  FileText,
} from "lucide-react";
import "../../../styles/OrderConfirmation.css";

export const OrderConfirmation = ({ orderData, onBackToMenu, onViewOrder }) => {
  const getPaymentMethodInfo = (method) => {
    switch (method) {
      case "mercadopago":
        return {
          icon: CreditCard,
          label: "Mercado Pago",
          status:
            orderData.paymentStatus === "approved"
              ? "Pago confirmado"
              : "Procesando pago",
        };
      case "cash":
        return {
          icon: HandCoins,
          label: "Pago en efectivo",
          status: "Pendiente de pago",
        };
      default:
        return {
          icon: Clock,
          label: "Método no especificado",
          status: "Pendiente",
        };
    }
  };

  const getOrderStatusInfo = (status) => {
    switch (status) {
      case "confirmed":
        return {
          color: "success",
          text: "Pedido confirmado",
          description: "Tu pedido ha sido recibido y está siendo preparado",
        };
      case "pending":
        return {
          color: "warning",
          text: "Pedido pendiente",
          description: "Estamos procesando tu pedido",
        };
      case "preparing":
        return {
          color: "info",
          text: "En preparación",
          description: "Tu pedido está siendo preparado con cuidado",
        };
      default:
        return {
          color: "secondary",
          text: "Estado desconocido",
          description: "Contacta con el establecimiento para más información",
        };
    }
  };

  const paymentInfo = getPaymentMethodInfo(orderData.paymentMethod);
  const statusInfo = getOrderStatusInfo(orderData.status);
  const PaymentIcon = paymentInfo.icon;

  const customer = orderData.customer || {};

  return (
    <div className="cart-container">
      {/* Header Banner - Consistente con pasos anteriores */}
      <div className="cart-header cart-header-banner">
        <div className="success-icon-banner">
          <CheckCircle size={48} />
        </div>
        <h1>🎉 ¡Pedido confirmado!</h1>
        <p className="cart-subtitle">
          Tu pedido #{orderData.orderNumber} ha sido registrado correctamente
        </p>
        <div className="step-indicator">
          <span className="step-number">3</span>
          <span className="step-text">de 3</span>
        </div>
      </div>

      {/* Contenido principal */}
      <div className="cart-content">
        <section className="order-confirmation-section">
          {/* Estado del pedido */}
          <div className="order-status-card-container">
            <div className={`status-badge ${statusInfo.color}`}>
              {statusInfo.text}
            </div>
            <p className="status-description">{statusInfo.description}</p>
          </div>

          {/* === Detalles del pedido === */}
          <div className="order-details-card">
            <h3 className="details-title">Detalles del pedido</h3>

            {/* Método de pago */}
            <div className="detail-row">
              <div className="detail-icon">
                <PaymentIcon size={20} />
              </div>
              <div className="detail-info">
                <span className="detail-label">Método de pago:</span>
                <span className="detail-value">{paymentInfo.label}</span>
                <span className="detail-status">{paymentInfo.status}</span>
              </div>
            </div>

            {/* Envío o retiro */}
            <div className="detail-row">
              <div className="detail-icon">
                {orderData.shippingType === "delivery" ? (
                  <MapPin size={20} />
                ) : (
                  <Store size={20} />
                )}
              </div>
              <div className="detail-info">
                <span className="detail-label">
                  {orderData.shippingType === "delivery"
                    ? "Envío a domicilio:"
                    : "Retiro en local:"}
                </span>
                <span className="detail-value">
                  {orderData.shippingType === "delivery"
                    ? `${orderData.address?.street || ""} ${
                        orderData.address?.number || ""
                      }`
                    : "Retiro en el local"}
                </span>
                <span className="detail-status">
                  {orderData.estimatedTime || "30-45 min"}
                </span>
              </div>
            </div>

            {/* Datos del cliente */}
            <div className="detail-row">
              <div className="detail-icon">
                <User size={20} />
              </div>
              <div className="detail-info">
                <span className="detail-label">Cliente:</span>
                <span className="detail-value">
                  {customer.name || "No especificado"}
                </span>
                {customer.document && (
                  <span className="detail-subinfo">
                    <FileText size={14} /> {customer.document}
                  </span>
                )}
                {customer.phone && (
                  <span className="detail-subinfo">
                    <Phone size={14} /> {customer.phone}
                  </span>
                )}
                {customer.observations && (
                  <p className="detail-subinfo observation">
                    <em>Nota: {customer.observations}</em>
                  </p>
                )}
              </div>
            </div>

            {/* Total */}
            <div className="detail-row total-row">
              <div className="detail-info">
                <div className="total">
                  <span className="detail-label">Total:</span>
                  <span className="detail-value total-amount">
                    ${orderData.total?.toLocaleString()}
                  </span>
                </div>
              </div>
            </div>
          </div>

          {/* Próximos pasos */}
          <div className="next-steps-card">
            <h3>¿Qué sigue ahora?</h3>
            <div className="steps-list">
              {orderData.paymentMethod === "mercadopago" &&
                orderData.paymentStatus !== "approved" && (
                  <div className="step-item">
                    <Clock size={16} />
                    <span>Esperamos la confirmación del pago</span>
                  </div>
                )}
              <div className="step-item">
                <Clock size={16} />
                <span>
                  Preparamos tu pedido ({orderData.estimatedTime || "30-45 min"}
                  )
                </span>
              </div>
              <div className="step-item">
                {orderData.shippingType === "delivery" ? (
                  <MapPin size={16} />
                ) : (
                  <Store size={16} />
                )}
                <span>
                  {orderData.shippingType === "delivery"
                    ? "Lo enviamos a tu domicilio"
                    : "Te avisamos cuando esté listo para retirar"}
                </span>
              </div>
            </div>
          </div>

          {/* Botones de acción */}
          <div className="confirmation-actions">
            <button onClick={onBackToMenu} className="secondary-button">
              <ArrowLeft size={16} />
              Volver al menú
            </button>

            {onViewOrder && (
              <button onClick={onViewOrder} className="primary-button">
                Ver mi pedido
              </button>
            )}
          </div>
        </section>
      </div>
    </div>
  );
};
