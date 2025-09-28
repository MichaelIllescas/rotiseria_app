import * as React from "react";
import { cn } from "./utils";

/**
 * 📌 Input
 *
 * Un campo de entrada de texto reutilizable que se puede usar en formularios.
 * Se le aplican estilos comunes para mantener consistencia visual en toda la app.
 *
 * ✅ ¿Para qué se usa?
 * - Para capturar datos de usuario: texto, email, password, números, etc.
 * - Puede recibir atributos estándar de un <input>.
 *
 * 🔹 Qué recibe (props):
 * - className: (string) → clases adicionales de CSS para personalizar estilos.
 * - type: (string) → define el tipo de input (text, email, password, number, file, etc).
 * - ...props: cualquier otro atributo nativo de <input> (placeholder, value, onChange, etc).
 */
function Input({ className, type, ...props }) {
  return (
    <input
      type={type}
      data-slot="input" // atributo de control interno para identificar el componente
      className={cn(
        // Estilos base
        "file:text-foreground placeholder:text-muted-foreground selection:bg-primary selection:text-primary-foreground dark:bg-input/30 border-input flex h-9 w-full min-w-0 rounded-md border px-3 py-1 text-base bg-input-background transition-[color,box-shadow] outline-none file:inline-flex file:h-7 file:border-0 file:bg-transparent file:text-sm file:font-medium disabled:pointer-events-none disabled:cursor-not-allowed disabled:opacity-50 md:text-sm",
        // Estilos de enfoque (cuando el usuario hace click o tab)
        "focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px]",
        // Estilos de error (cuando el input no es válido)
        "aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive",
        className
      )}
      {...props}
    />
  );
}

export { Input };
