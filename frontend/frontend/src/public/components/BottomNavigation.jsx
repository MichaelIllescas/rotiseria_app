import React, { useState, useEffect } from "react";
import "../styles/BottomNavigation.css";

export const BottomNavigation = () => {
  const [activeSection, setActiveSection] = useState("inicio");

  // Detectar sección activa al hacer scroll
  useEffect(() => {
    const handleScroll = () => {
      const sections = ["inicio", "menu", "nosotros", "contacto"];
      const scrollPosition = window.scrollY + 200;

      for (const section of sections) {
        const element = document.getElementById(section);
        if (element) {
          const { offsetTop, offsetHeight } = element;
          if (scrollPosition >= offsetTop && scrollPosition < offsetTop + offsetHeight) {
            setActiveSection(section);
            break;
          }
        }
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const scrollToSection = (sectionId) => {
    const element = document.getElementById(sectionId);
    if (element) {
      element.scrollIntoView({ 
        behavior: "smooth",
        block: "start"
      });
    }
  };

  const menuItems = [
    {
      id: "inicio",
      icon: "🏠",
      label: "Inicio",
      action: () => scrollToSection("inicio")
    },
    {
      id: "menu",
      icon: "🍽️",
      label: "Menú",
      action: () => scrollToSection("menu")
    },
    {
      id: "nosotros",
      icon: "👥",
      label: "Nosotros",
      action: () => scrollToSection("nosotros")
    },
    {
      id: "contacto",
      icon: "📞",
      label: "Contacto",
      action: () => scrollToSection("contacto")
    },
    {
      id: "whatsapp",
      icon: "💬",
      label: "WhatsApp",
      action: () => window.open("https://wa.me/5491145678900?text=Hola,%20me%20interesa%20hacer%20un%20pedido", "_blank")
    }
  ];

  return (
    <nav className="bottom-navigation">
      <div className="bottom-nav-container">
        {menuItems.map((item) => (
          <button
            key={item.id}
            className={`bottom-nav-item ${activeSection === item.id ? "active" : ""}`}
            onClick={item.action}
            type="button"
          >
            <span className="nav-icon">{item.icon}</span>
            <span className="nav-label">{item.label}</span>
            {activeSection === item.id && <div className="active-indicator" />}
          </button>
        ))}
      </div>
    </nav>
  );
};