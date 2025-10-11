import React, { useState } from "react";
import "../styles/ContactSection.css";

export const ContactSection = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    message: ""
  });

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
          
          <div className="contact-item">
            <div className="contact-icon">📍</div>
            <div className="contact-details">
              <h4>Dirección</h4>
              <p>Av. Corrientes 1234<br />Villa Crespo, CABA<br />Argentina</p>
            </div>
          </div>

          <div className="contact-item">
            <div className="contact-icon">📞</div>
            <div className="contact-details">
              <h4>Teléfono</h4>
              <p>+54 11 4567-8900</p>
              <p>WhatsApp: +54 9 11 4567-8900</p>
            </div>
          </div>

          <div className="contact-item">
            <div className="contact-icon">✉️</div>
            <div className="contact-details">
              <h4>Email</h4>
              <p>info@foodstore.com.ar</p>
              <p>pedidos@foodstore.com.ar</p>
            </div>
          </div>

          <div className="contact-item">
            <div className="contact-icon">⏰</div>
            <div className="contact-details">
              <h4>Horarios de Atención</h4>
              <p><strong>Lunes a Viernes:</strong> 11:00 - 22:00</p>
              <p><strong>Sábados:</strong> 11:00 - 23:00</p>
              <p><strong>Domingos:</strong> 12:00 - 22:00</p>
            </div>
          </div>

          <div className="social-links">
            <h4>Síguenos</h4>
            <div className="social-buttons">
              <a href="#" className="social-btn facebook">📘 Facebook</a>
              <a href="#" className="social-btn instagram">📷 Instagram</a>
              <a href="#" className="social-btn whatsapp">💬 WhatsApp</a>
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

      {/* Mapa de Google Maps */}
      <div className="location-info">
        <h3>📍 Nuestra Ubicación</h3>
        <div className="map-container">
          <iframe
            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3284.4156165924627!2d-58.44421908476186!3d-34.59884598046416!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x95bcb5901e4c8fbb%3A0x7a9a8e8e8e8e8e8e!2sAv.%20Corrientes%201234%2C%20C1414%20CABA!5e0!3m2!1ses!2sar!4v1697834567890!5m2!1ses!2sar"
            width="100%"
            height="300"
            style={{ border: 0 }}
            allowFullScreen=""
            loading="lazy"
            referrerPolicy="no-referrer-when-downgrade"
            title="Ubicación de FoodStore"
          ></iframe>
        </div>
        <div className="map-info">
          <p>📍 <strong>Av. Corrientes 1234, Villa Crespo, CABA</strong></p>
          <p>🚇 <strong>Metro:</strong> Línea B - Estación Carlos Gardel (3 cuadras)</p>
          <p>🚌 <strong>Colectivos:</strong> 15, 19, 41, 55, 71, 109, 168</p>
          <div className="map-actions">
            <a 
              href="https://maps.google.com/maps?q=Av.+Corrientes+1234,+CABA" 
              target="_blank" 
              rel="noopener noreferrer"
              className="map-btn"
            >
              🗺️ Ver en Google Maps
            </a>
            <a 
              href="https://www.google.com/maps/dir//Av.+Corrientes+1234,+CABA" 
              target="_blank" 
              rel="noopener noreferrer"
              className="map-btn"
            >
              🧭 Cómo llegar
            </a>
          </div>
        </div>
      </div>
    </div>
  );
};