import React, { useState, useEffect, useCallback } from "react";
import { Link, useNavigate } from "react-router-dom";
import { ShoppingCart, ArrowLeft } from "lucide-react";
import { useCart } from "../../context/CartContext";
import { CartItem } from "../components/cart/components/CartItem";
import { CartSummary } from "../components/cart/components/CartSummary";
import { PaymentMethod } from "../components/cart/components/PaymentMethod";
import { OrderConfirmation } from "../components/cart/components/OrderConfirmation";
import { OrderDetails } from "../components/cart/components/OrderDetails";
import { ShippingSelector } from "../components/cart/components/ShippingSelector";
import { CustomerInfo } from "../components/cart/components/CustomerInfo";
import "../styles/CartPage.css";
import { BottomNavigation } from "../components/BottomNavigation";

export const CartPage = () => {
  const { cart, total, removeFromCart, updateQuantity, clearCart } = useCart();
  const navigate = useNavigate();

//costo de envio
const SHIPPING_COST = 3500;

  // Flujo de pasos
  const [currentStep, setCurrentStep] = useState(1);
  const [showOrderDetails, setShowOrderDetails] = useState(false);

  // Datos del envío y cliente
  const [shippingData, setShippingData] = useState({ 
    isValid: false,
    id: "pickup",
    type: "pickup",
    title: "Retiro en Sucursal",
    cost: 0,
    price: 0
  });
  const [customerData, setCustomerData] = useState({ isValid: false });

  

  // Estado general de validación (unificado)
  const [isCheckoutReady, setIsCheckoutReady] = useState(false);

  // Datos del pedido
  const [orderData, setOrderData] = useState(null);

  // Actualiza el estado global según las validaciones de los subcomponentes
  useEffect(() => {
 
    setIsCheckoutReady(
      shippingData.isValid && customerData.isValid && cart.length > 0
    );
  }, [shippingData, customerData, cart]);

  // Navegación entre pasos
  const handleContinueToPayment = () => setCurrentStep(2);
  const handleBackToCart = () => setCurrentStep(1);
  const handleBackToMenu = () => navigate("/");
  
  // Navegación para OrderDetails
  const handleViewOrder = () => setShowOrderDetails(true);
  const handleBackToConfirmation = () => setShowOrderDetails(false);
  const handleReorder = () => {
    // Lógica para reordenar (agregar productos al carrito)
    console.log("Reordenar productos:", orderData);
    setCurrentStep(1);
    setShowOrderDetails(false);
  };
  const handleContactSupport = () => {
    // Lógica para contactar soporte (WhatsApp, teléfono, etc.)
    console.log("Contactar soporte");
    // Ejemplo: window.open('https://wa.me/1234567890', '_blank');
  };

  // 🧾 Crear orden de compra
  const createOrder = (paymentMethod, paymentStatus) => {
    const newOrder = {
      orderNumber: Math.floor(Math.random() * 1000000)
        .toString()
        .padStart(6, "0"),
      items: cart,
      products: cart.map(item => ({
        id: item.id,
        name: item.name,
        description: item.description || 'Producto delicioso',
        price: item.price,
        quantity: item.quantity,
        modifications: item.modifications || []
      })),
      total: total + (shippingData?.cost || 0),
      shippingCost: shippingData?.cost || 0,
      paymentMethod,
      paymentStatus,
      status: "confirmed",
      shippingType: shippingData?.type || "pickup",
      address: shippingData?.address,
      estimatedTime: shippingData?.estimatedTime || "30-45 min",
      customer: {
        name: customerData?.name || "Cliente",
        phone: customerData?.phone || "",
        email: customerData?.email || "",
        document: customerData?.document || "",
        observations: customerData?.observations || "",
      },
      createdAt: new Date().toISOString(),
    };

    setOrderData(newOrder);
    setCurrentStep(3);
    clearCart();
  };

  // 💳 Procesar método de pago
  const handlePaymentSelect = async (payment) => {
    if (payment.method === "mercadopago") {
      try {
        const response = await fetch("/api/mercadopago/create-preference", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            items: cart,
            total,
            shipping: shippingData,
          }),
        });

        const data = await response.json();

        if (data.init_point) {
          window.location.href = data.init_point;
        } else {
          createOrder("mercadopago", "pending");
        }
      } catch (error) {
        console.error("Error al crear preferencia de Mercado Pago:", error);
        createOrder("mercadopago", "pending");
      }
    } else {
      createOrder("cash", "pending");
    }
  };

  // Recibir cambios desde subcomponentes
  const handleShippingChange = useCallback((shipping) => {
    console.log('CartPage - handleShippingChange received:', shipping);
    setShippingData(shipping);
  }, []);
  
  const handleCustomerChange = useCallback((customer) => {
    setCustomerData(customer);
  }, []);

  // 🛒 Carrito vacío
  if (cart.length === 0 && currentStep === 1) {
    return (
      <div className="cart-page">
        <div className="cart-container">
          <div className="empty-cart">
            <div className="empty-cart-icon">🛒</div>
            <h3>Tu carrito está vacío</h3>
            <p>
              Parece que aún no agregaste ningún producto. ¡Explorá nuestro menú
              y encontrá tus platos favoritos!
            </p>
            <Link to="/" className="continue-shopping-btn">
              <ArrowLeft size={20} style={{ marginRight: "0.5rem" }} />
              Explorar Menú
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="cart-page">
      <div className="cart-container">
        {/* Paso 1: Carrito y datos */}
        {currentStep === 1 && (
          <>
            <div className="cart-header cart-header-banner">
              <h1>🛍️ ¡Tu pedido está por comenzar!</h1>
              <p className="cart-subtitle">
                Revisá tu carrito y completá los datos para continuar
              </p>
              <div className="step-indicator">
                <span className="step-number">1</span>
                <span className="step-text">de 3</span>
              </div>
            </div>

            <div className="cart-content flex flex-col gap-6">
              {/* 🛒 Productos */}
              <section className="cart-items-section">
                <div className="cart-items-header">
                  <ShoppingCart size={20} style={{ marginRight: "0.5rem" }} />
                  Productos en tu carrito ({cart.length}{" "}
                  {cart.length === 1 ? "producto" : "productos"})
                </div>
                <div className="cart-items-list">
                  {cart.map((item) => (
                    <CartItem
                      key={item.id}
                      item={item}
                      onRemove={removeFromCart}
                      onUpdateQuantity={updateQuantity}
                    />
                  ))}
                </div>
              </section>

              {/* 🚚 Envío */}
              <section className="cart-shipping-section">
                <ShippingSelector onShippingChange={handleShippingChange} shippingCost={SHIPPING_COST} />
              </section>

              {/* 👤 Datos del cliente */}
              <section className="cart-customer-section">
                <CustomerInfo
                  onCustomerChange={handleCustomerChange}
                  shippingType={shippingData?.type || "pickup"}
                />
              </section>

              {/* 💵 Resumen */}
              <section className="cart-summary-section">
                <CartSummary
                  cart={cart}
                  total={total}
                  onClear={clearCart}
                  onContinueToPayment={handleContinueToPayment}
                  isCheckoutReady={isCheckoutReady}
                  shippingCost={shippingData?.cost }
                />
              </section>
            </div>
          </>
        )}

        {/* Paso 2: Método de pago */}
        {currentStep === 2 && (
          <PaymentMethod
            total={total}
            shippingData={shippingData}
            onPaymentSelect={handlePaymentSelect}
            onBack={handleBackToCart}
          />
        )}

        {/* Paso 3: Confirmación o Detalles del pedido */}
        {currentStep === 3 && orderData && !showOrderDetails && (
          <OrderConfirmation
            orderData={orderData}
            onBackToMenu={handleBackToMenu}
            onViewOrder={handleViewOrder}
          />
        )}

        {/* Detalles del pedido */}
        {currentStep === 3 && orderData && showOrderDetails && (
          <OrderDetails
            orderData={orderData}
            onBack={handleBackToConfirmation}
            onReorder={handleReorder}
            onContactSupport={handleContactSupport}
          />
        )}
      </div>
    </div>
  );
};
