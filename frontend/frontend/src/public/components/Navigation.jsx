import React, { useState } from 'react';
import { Menu, X, Home, Info, Phone, ShoppingBag, ShoppingCart, User } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import '../styles/Navigation.css';

const Navigation = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const { user, login, logout } = useAuth();

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const closeMenu = () => {
    setIsMenuOpen(false);
  };

  const handleAuthAction = () => {
    if (user) {
      logout();
    } else {
      login();
    }
  };

  return (
    <nav className="main-navigation">
      <div className="nav-container">
        {/* Logo */}
        <div className="brand-logo">
          <h1> FoodStore</h1>
        </div>

        {/* Desktop Navigation */}
        <div className="desktop-menu">
          <ul className="menu-list">
            <li className="menu-item">
              <a href="#inicio" className="menu-link">
                <Home size={18} />
                <span>Inicio</span>
              </a>
            </li>
            <li className="menu-item">
              <a href="#menu" className="menu-link">
                <ShoppingBag size={18} />
                <span>Menú</span>
              </a>
            </li>
            <li className="menu-item">
              <a href="#nosotros" className="menu-link">
                <Info size={18} />
                <span>Nosotros</span>
              </a>
            </li>
            <li className="menu-item">
              <a href="#contacto" className="menu-link">
                <Phone size={18} />
                <span>Contacto</span>
              </a>
            </li>
          </ul>
        </div>

        {/* Ícono de carrito al extremo derecho */}
        <div className="cart-icon">
          <ShoppingCart size={24} />
        </div>

        {/* Mobile Menu Button */}
        <button className="mobile-toggle" onClick={toggleMenu}>
          {isMenuOpen ? <X size={24} /> : <Menu size={24} />}
        </button>
      </div>

      {/* Mobile Navigation */}
      <div className={`mobile-menu ${isMenuOpen ? 'mobile-menu-active' : ''}`}>
        <ul className="mobile-list">
          <li className="mobile-item">
            <a href="#inicio" className="mobile-link" onClick={closeMenu}>
              <Home size={20} />
              <span>Inicio</span>
            </a>
          </li>
          <li className="mobile-item">
            <a href="#menu" className="mobile-link" onClick={closeMenu}>
              <ShoppingBag size={20} />
              <span>Menú</span>
            </a>
          </li>
          <li className="mobile-item">
            <a href="#nosotros" className="mobile-link" onClick={closeMenu}>
              <Info size={20} />
              <span>Nosotros</span>
            </a>
          </li>
          <li className="mobile-item">
            <a href="#contacto" className="mobile-link" onClick={closeMenu}>
              <Phone size={20} />
              <span>Contacto</span>
            </a>
          </li>
        </ul>
      </div>

      {/* Mobile Menu Overlay */}
      {isMenuOpen && <div className="menu-backdrop" onClick={closeMenu}></div>}
    </nav>
  );
};

export default Navigation;
