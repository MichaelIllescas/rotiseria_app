import * as React from "react";
import { cn } from "./utils";

/**
 * 📌 Textarea
 *
 * Campo de texto multilínea reutilizable, estilizado y accesible.
 * Se usa para inputs largos como comentarios, descripciones, mensajes, etc.
 *
 * ✅ Props (heredadas de `<textarea>`):
 * - placeholder: string → texto de ayuda cuando está vacío.
 * - value / defaultValue: string → valor controlado o inicial.
 * - onChange: (event) => void → callback cuando cambia el contenido.
 * - disabled?: boolean → deshabilita la interacción.
 * - aria-invalid?: boolean → marca el campo como inválido (para accesibilidad).
 * - className?: string → clases extra para personalización.
 * - ...props: cualquier otro atributo nativo de un `<textarea>`.
 *
 * 🎨 Estilos aplicados:
 * - `resize-none` → deshabilita el redimensionamiento manual.
 * - `border-input` + `bg-input-background` → estilos base del tema.
 * - `focus-visible:*` → borde y anillo cuando recibe foco.
 * - `aria-invalid:*` → estilos cuando el campo no pasa validación.
 * - `disabled:*` → opacidad y cursor al deshabilitarse.
 * - `min-h-16` → altura mínima para evitar que quede demasiado chico.
 * - `field-sizing-content` → ajusta tamaño en función del contenido (si el navegador lo soporta).
 *
 * ⚠️ Si no se pasa `placeholder` ni `value`, se verá como un campo vacío sin texto de ayuda.
 */
function Textarea({ className, ...props }) {
  return (
    <textarea
      data-slot="textarea" // atributo útil para testing/debug
      className={cn(
        "resize-none border-input placeholder:text-muted-foreground focus-visible:border-ring focus-visible:ring-ring/50 aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive dark:bg-input/30 flex field-sizing-content min-h-16 w-full rounded-md border bg-input-background px-3 py-2 text-base transition-[color,box-shadow] outline-none focus-visible:ring-[3px] disabled:cursor-not-allowed disabled:opacity-50 md:text-sm",
        className // clases adicionales del usuario
      )}
      {...props} // props nativos de <textarea>
    />
  );
}

export { Textarea };
