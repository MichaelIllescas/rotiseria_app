import React from "react";
import "../styles/AboutSection.css";

export const AboutSection = () => {
  return (
    <div className="about-content">
      <h2 className="section-heading">Sobre Nosotros</h2>
      <div className="about-story">
        <h3>Nuestra Historia</h3>
        <p>
          FoodStore nació en 2023 como un emprendimiento familiar en el corazón de Buenos Aires. 
          Con la pasión por la cocina casera y el deseo de brindar comida de calidad, 
          comenzamos preparando platos tradicionales argentinos en nuestra pequeña cocina.
        </p>
        <p>
          Lo que empezó como un sueño de ofrecer sabores auténticos, hoy se ha convertido en 
          el lugar de confianza para más de 2,500 familias que buscan la calidez y el sabor 
          de la comida hecha en casa.
        </p>
      </div>

      <div className="about-values">
        <h3>Lo Que Nos Define</h3>
        <div className="values-grid">
          <div className="value-item">
            <h4>🥘 Ingredientes Frescos</h4>
            <p>Seleccionamos diariamente los mejores productos de mercados locales</p>
          </div>
          <div className="value-item">
            <h4>👨‍🍳 Recetas Tradicionales</h4>
            <p>Preparamos cada plato siguiendo recetas familiares transmitidas por generaciones</p>
          </div>
          <div className="value-item">
            <h4>🌱 Compromiso Local</h4>
            <p>Apoyamos a productores de la región y practicamos cocina sustentable</p>
          </div>
          <div className="value-item">
            <h4>❤️ Atención Personal</h4>
            <p>Cada cliente es parte de nuestra familia extendida</p>
          </div>
        </div>
      </div>

      <div className="about-stats">
        <h3>En Números</h3>
        <div className="stats-grid">
          <div className="stat-item">
            <span className="stat-number">2,500+</span>
            <span className="stat-label">Clientes Satisfechos</span>
          </div>
          <div className="stat-item">
            <span className="stat-number">18</span>
            <span className="stat-label">Especialidades del Menú</span>
          </div>
          <div className="stat-item">
            <span className="stat-number">100%</span>
            <span className="stat-label">Cocina Casera</span>
          </div>
          <div className="stat-item">
            <span className="stat-number">2023</span>
            <span className="stat-label">Año de Fundación</span>
          </div>
        </div>
      </div>

      <div className="about-mission">
        <h3>Nuestra Filosofía</h3>
        <blockquote>
          "Creemos que la buena comida une a las personas. Por eso, cada plato que preparamos 
          lleva el cariño y la dedicación de una cocina familiar, manteniendo vivos los sabores 
          que nos conectan con nuestras raíces y tradiciones."
        </blockquote>
        <div className="mission-highlight">
          <p>🍽️ Comida casera auténtica</p>
          <p>⏰ Preparada diariamente</p>
          <p>🏠 Con el sabor del hogar</p>
        </div>
      </div>
    </div>
  );
};