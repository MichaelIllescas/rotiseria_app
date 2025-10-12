import Navigation from "../components/Navigation";
import "../styles/HomePage.css";
import { SocialSidebar } from "../components/SocialSidebar";
import { useCategories } from "../hooks/useCategories";
import { ProductTabs } from "../components/ProductTabs";
import { useProducts } from "../hooks/useProducts";
import { ProductSearch } from "../components/ProductSearch";
import { AboutSection } from "../components/AboutSection";
import { ContactSection } from "../components/ContactSection";
import { BottomNavigation } from "../components/BottomNavigation";
import { useState } from "react";
import { useBusinessData } from "../hooks/useBusinessData";

const HomePage = () => {
  const { categories, loading, error } = useCategories();
  const { products } = useProducts();
  const [searchTerm, setSearchTerm] = useState("");
  const { businessData } = useBusinessData();

  if (loading) {
    return (
      <div className="home-page">
        <div className="nav-wrapper">
          <Navigation />
          <SocialSidebar />
        </div>
        <main className="page-content">
          <p>Cargando...</p>
        </main>
      </div>
    );
  }

  if (error) {
    return (
      <div className="home-page">
        <div className="nav-wrapper">
          <Navigation />
          <SocialSidebar />
        </div>
        <main className="page-content">
          <p>Error: {error}</p>
        </main>
      </div>
    );
  }

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
            <div className="hero-content">
              <div className="hero-text">
                <h1 className="hero-heading">Bienvenidos a FoodStore</h1>
                <p className="hero-description">
                  Comida casera preparada con amor y los mejores ingredientes
                </p>
                <button className="hero-button">Ver Nuestro Menú</button>
              </div>
              <ul className="hero-categories">
                {categories.map((category) => (
                  <li key={category.id}>{category.name}</li>
                ))}
              </ul>
            </div>
          </div>
        </section>
        <hr />

        {/* Menu Section */}
        <section id="menu" className="menu-showcase">
          <div className="content-wrapper">
            <h2 className="section-heading">Nuestro Menú</h2>
            <p className="section-description">
              Descubre nuestras especialidades preparadas diariamente
            </p>
            <ProductSearch
              searchTerm={searchTerm}
              onSearchChange={setSearchTerm}
            />
            <ProductTabs
              categories={categories || []}
              products={products || []}
              searchTerm={searchTerm}
            />
          </div>
        </section>
        <hr />
        {/* About Section */}
        <section id="nosotros" className="about-showcase">
          <div className="content-wrapper">
          
            <AboutSection  />
          </div>
        </section>

        <hr />
        {/* Contact Section */}
        <section id="contacto" className="contact-showcase">
          <div className="content-wrapper">

            <ContactSection business={businessData} />
          </div>
        </section>
         {/* Bottom Navigation */}
      </main>

      {/* Footer */}
      <footer className="page-footer">
        <div className="footer-wrapper">
          <p>&copy; 2024 FoodStore. Todos los derechos reservados.</p>
        </div>
      </footer>

      <BottomNavigation />
     
    </div>
  );
};

export default HomePage;
