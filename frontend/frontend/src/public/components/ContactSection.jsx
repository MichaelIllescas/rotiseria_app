import React, { useState } from "react";
import "../styles/ContactSection.css";
import { BusinessHoursViewer } from "../../modules/business/components/BusinessHoursViewer";
import { useBusinessHours } from "../hooks/useBusinessHour";

export const ContactSection = ({ business = {} }) => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    message: ""
  });

  const { businessHours, loading, error } = useBusinessHours();

  // Función para generar URL del mapa basada en la dirección
  const generateMapUrl = (address) => {
    if (!address) return "";
    const encodedAddress = encodeURIComponent(address);
    return `https://www.google.com/maps/embed/v1/place?key=${process.env.REACT_APP_GOOGLE_MAPS_API_KEY || ''}&q=${encodedAddress}`;
  };

  // Función alternativa para mapa sin API key (usando search)
  const generateMapUrlFallback = (address) => {
    if (!address) return "";
    const encodedAddress = encodeURIComponent(address);
    return `https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3284.4156165924627!2d-58.44421908476186!3d-34.59884598046416!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x95bcb5901e4c8fbb%3A0x7a9a8e8e8e8e8e8e!2s${encodedAddress}!5e0!3m2!1ses!2sar!4v1697834567890!5m2!1ses!2sar`;
  };

  // URLs para direcciones externas
  const getGoogleMapsUrl = (address) => {
    if (!address) return "#";
    return `https://maps.google.com/maps?q=${encodeURIComponent(address)}`;
  };

  const getDirectionsUrl = (address) => {
    if (!address) return "#";
    return `https://www.google.com/maps/dir//${encodeURIComponent(address)}`;
  };

  // Función para formatear número de WhatsApp
  const formatWhatsAppNumber = (phone) => {
    if (!phone) return "";
    // Remover espacios, guiones y paréntesis
    const cleanPhone = phone.replace(/[\s\-\(\)]/g, "");
    // Si no empieza con +, agregar código de país (Argentina)
    if (!cleanPhone.startsWith("+")) {
      return `+54${cleanPhone}`;
    }
    return cleanPhone;
  };

  // URL de WhatsApp
  const getWhatsAppUrl = (phone) => {
    const formattedPhone = formatWhatsAppNumber(phone);
    return `https://wa.me/${formattedPhone.replace("+", "")}`;
  };

  if (loading) return <p>Cargando información...</p>;
  if (error) return <p>Error al cargar información: {error}</p>;

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Aquí podrías enviar los datos a tu backend
    console.log("Formulario enviado:", formData);
    // Resetear formulario
    setFormData({
      name: "",
      email: "",
      phone: "",
      message: ""
    });
    alert("¡Mensaje enviado! Te contactaremos pronto.");
  };

  return (
    <div className="contact-content">
      <h2 className="section-heading">Contacto</h2>
      <div className="contact-grid">
        {/* Información de Contacto */}
        <div className="contact-info">
          <h3>Información</h3>
          
          {business.address && (
            <div className="contact-item">
              <div className="contact-icon">📍</div>
              <div className="contact-details">
                <h4>Dirección</h4>
                <p>{business.address}</p>
              </div>
            </div>
          )}

          {business.phone && (
            <div className="contact-item">
              <div className="contact-icon">📞</div>
              <div className="contact-details">
                <h4>Teléfono</h4>
                <p>{business.phone}</p>
                <p>
                  <a 
                    href={getWhatsAppUrl(business.phone)} 
                    target="_blank" 
                    rel="noopener noreferrer"
                    className="whatsapp-link"
                  >
                    WhatsApp: {business.phone}
                  </a>
                </p>
              </div>
            </div>
          )}

          {business.email && (
            <div className="contact-item">
              <div className="contact-icon">✉️</div>
              <div className="contact-details">
                <h4>Email</h4>
                <p>
                  <a href={`mailto:${business.email}`} className="email-link">
                    {business.email}
                  </a>
                </p>
              </div>
            </div>
          )}

          {businessHours && businessHours.length > 0 && (
            <div className="contact-item">
              <div className="contact-icon">⏰</div>
              <div className="contact-details">
                <h4>Horarios de Atención</h4>
                <div className="hours">
                  <BusinessHoursViewer hours={businessHours} />
                </div>
              </div>
            </div>
          )}

          <div className="social-links">
            <h4>Síguenos</h4>
            <div className="social-buttons">
              {business.facebookUrl && (
                <a 
                  href={business.facebookUrl} 
                  target="_blank" 
                  rel="noopener noreferrer"
                  className="social-btn facebook"
                >
                  📘 Facebook
                </a>
              )}
              
              {business.instagramUrl && (
                <a 
                  href={business.instagramUrl} 
                  target="_blank" 
                  rel="noopener noreferrer"
                  className="social-btn instagram"
                >
                  📷 Instagram
                </a>
              )}
              
              {business.phone && (
                <a 
                  href={getWhatsAppUrl(business.phone)} 
                  target="_blank" 
                  rel="noopener noreferrer"
                  className="social-btn whatsapp"
                >
                  💬 WhatsApp
                </a>
              )}
            </div>
          </div>
        </div>

        {/* Formulario de Contacto */}
        <div className="contact-form-wrapper">
          <h3>Envíanos un Mensaje</h3>
          <form className="contact-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="name">Nombre completo *</label>
              <input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                required
                placeholder="Tu nombre completo"
              />
            </div>

            <div className="form-group">
              <label htmlFor="email">Email *</label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                required
                placeholder="tu@email.com"
              />
            </div>

            <div className="form-group">
              <label htmlFor="phone">Teléfono</label>
              <input
                type="tel"
                id="phone"
                name="phone"
                value={formData.phone}
                onChange={handleInputChange}
                placeholder="+54 11 1234-5678"
              />
            </div>

            <div className="form-group">
              <label htmlFor="message">Mensaje *</label>
              <textarea
                id="message"
                name="message"
                value={formData.message}
                onChange={handleInputChange}
                required
                rows="5"
                placeholder="Cuéntanos en qué podemos ayudarte..."
              ></textarea>
            </div>

            <button type="submit" className="submit-btn">
              Enviar Mensaje
            </button>
          </form>
        </div>
      </div>

      {/* Mapa de Google Maps - Solo si hay dirección */}
      {business.address && (
        <div className="location-info">
          <h3>📍 Nuestra Ubicación</h3>
          <div className="map-container">
            <iframe
              src={generateMapUrlFallback(business.address)}
              width="100%"
              height="300"
              style={{ border: 0 }}
              allowFullScreen=""
              loading="lazy"
              referrerPolicy="no-referrer-when-downgrade"
              title={`Ubicación de ${business.name || 'Nuestro Negocio'}`}
            ></iframe>
          </div>
          <div className="map-info">
            <p>📍 <strong>{business.address}</strong></p>
            
            {business.nearbyTransport && (
              <div className="transport-info">
                {business.nearbyTransport.metro && (
                  <p>🚇 <strong>Metro:</strong> {business.nearbyTransport.metro}</p>
                )}
                {business.nearbyTransport.buses && (
                  <p>🚌 <strong>Colectivos:</strong> {business.nearbyTransport.buses}</p>
                )}
              </div>
            )}
            
            <div className="map-actions">
              <a 
                href={getGoogleMapsUrl(business.address)} 
                target="_blank" 
                rel="noopener noreferrer"
                className="map-btn"
              >
                🗺️ Ver en Google Maps
              </a>
              <a 
                href={getDirectionsUrl(business.address)} 
                target="_blank" 
                rel="noopener noreferrer"
                className="map-btn"
              >
                🧭 Cómo llegar
              </a>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};