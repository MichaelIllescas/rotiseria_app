"use client";

import { useTheme } from "next-themes";
import { Toaster as Sonner,toast as sonnerToast } from "sonner";

/**
 * 📌 Toaster
 *
 * Contenedor global para notificaciones (basado en Sonner).
 * Aplica automáticamente el tema (light/dark/system) usando `next-themes`.
 *
 * ✅ Props principales:
 * - Se heredan todos los de Sonner (ej: position, duration, etc.).
 * - Se puede personalizar tema con `theme`.
 *
 * ⚠️ Importante:
 * - Debe colocarse en el root layout de tu aplicación → así las notificaciones
 *   estarán disponibles en todas las páginas.
 */
const Toaster = ({ ...props }) => {
  // Hook que devuelve el tema actual → "light", "dark" o "system"
  const { theme = "system" } = useTheme();

  return (
    <Sonner
      // Se castea el theme a un valor válido para Sonner
      theme={theme}
      className="toaster group"
      // CSS custom properties para colores personalizados
      style={{
        "--normal-bg": "var(--popover)",
        "--normal-text": "var(--popover-foreground)",
        "--normal-border": "var(--border)",
      }}
      {...props}
    />
  );
};

export { Toaster, sonnerToast as toast };
