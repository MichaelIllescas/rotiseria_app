import React, { useEffect } from "react";
import Navigation from "../components/Navigation";
import "../styles/HomePage.css";
import { SocialSidebar } from "../components/SocialSidebar";
import { useCategories } from "../../modules/category/hooks/useCategories";

const HomePage = () => {
  const { categories } = useCategories();
 
console.log(categories);

    return (
    <div className="home-page">
      <div className="nav-wrapper">
        <Navigation />
        <SocialSidebar />
      </div>

      <main className="page-content">
        {/* Hero Section */}
        <section id="inicio" className="hero-banner">
          <div className="hero-wrapper">
            {/* Contenedor principal dividido en dos columnas */}
            <div className="hero-content">
              {/* Texto del hero */}
              <div className="hero-text">
                <h1 className="hero-heading">Bienvenidos a FoodStore</h1>
                <p className="hero-description">
                  Comida casera preparada con amor y los mejores ingredientes
                </p>
                <button className="hero-button">Ver Nuestro Menú</button>
              </div>

              {/* Listado de categorías a la derecha */}
              <ul className="hero-categories">
                {categories.map((category) => (
                  <li key={category.id}>{category.name}</li>
                ))}
              </ul>
            </div>
          </div>
        </section>

        {/* Menu Section */}
        <section id="menu" className="menu-showcase">
          <div className="content-wrapper">
            <h2 className="section-heading">Nuestro Menú</h2>
            <p className="section-description">
              Descubre nuestras especialidades preparadas diariamente
            </p>
            {/* Menu content will go here */}
          </div>
        </section>

        {/* About Section */}
        <section id="nosotros" className="about-showcase">
          <div className="content-wrapper">
            <h2 className="section-heading">Sobre Nosotros</h2>
            <p className="section-description">
              Conoce nuestra historia y compromiso con la calidad
            </p>
            {/* About content will go here */}
          </div>
        </section>

        {/* Contact Section */}
        <section id="contacto" className="contact-showcase">
          <div className="content-wrapper">
            <h2 className="section-heading">Contacto</h2>
            <p className="section-description">Estamos aquí para atenderte</p>
            {/* Contact content will go here */}
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer className="page-footer">
        <div className="footer-wrapper">
          <p>&copy; 2024 Rotisería La Casa. Todos los derechos reservados.</p>
        </div>
      </footer>
    </div>
  );
};

export default HomePage;
