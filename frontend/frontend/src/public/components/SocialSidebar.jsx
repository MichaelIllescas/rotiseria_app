import React from "react";
import { FaFacebookF, FaInstagram, FaWhatsapp } from "react-icons/fa";
import "../styles/SocialSidebar.css";

export const SocialSidebar = () => {
  return (
    <div className="social-sidebar">
      <a
        href={import.meta.env.VITE_FACEBOOK_URL || "https://www.facebook.com/"}
        target="_blank"
        rel="noopener noreferrer"
        aria-label="Facebook"
        className="social-icon"
      >
        <FaFacebookF />
      </a>
      <a
        href={import.meta.env.VITE_INSTAGRAM_URL || "https://www.instagram.com/"}
        target="_blank"
        rel="noopener noreferrer"
        aria-label="Instagram"
        className="social-icon"
      >
        <FaInstagram />
      </a>
      <a
        href={`${import.meta.env.VITE_WHATSAPP_BASE_URL || "https://wa.me/"}${import.meta.env.VITE_WHATSAPP_PHONE || "5491234567890"}`}
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
