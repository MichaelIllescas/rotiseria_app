import React from "react";
import { FaFacebookF, FaInstagram, FaWhatsapp } from "react-icons/fa";
import "../styles/SocialSidebar.css";

export const SocialSidebar = () => {
  return (
    <div className="social-sidebar">
      <a
        href="https://www.facebook.com/"
        target="_blank"
        rel="noopener noreferrer"
        aria-label="Facebook"
        className="social-icon"
      >
        <FaFacebookF />
      </a>
      <a
        href="https://www.instagram.com/"
        target="_blank"
        rel="noopener noreferrer"
        aria-label="Instagram"
        className="social-icon"
      >
        <FaInstagram />
      </a>
      <a
        href="https://wa.me/5491234567890"
        target="_blank"
        rel="noopener noreferrer"
        aria-label="WhatsApp"
        className="social-icon"
      >
        <FaWhatsapp />
      </a>
    </div>
  );
};
