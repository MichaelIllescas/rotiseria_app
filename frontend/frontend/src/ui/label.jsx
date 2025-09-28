import * as React from "react";
import * as LabelPrimitive from "@radix-ui/react-label";
import { cn } from "./utils";

/**
 * 📌 Label
 *
 * Componente reutilizable para mostrar etiquetas de campos de formulario.
 * Se basa en Radix UI `LabelPrimitive.Root`, lo que garantiza accesibilidad
 * y compatibilidad con `input`, `textarea` y otros controles de formulario.
 *
 * ✅ ¿Para qué se usa?
 * - Para describir un campo de formulario.
 * - Mejora la accesibilidad porque asocia el texto visible con el campo controlado.
 *
 * 🔹 Qué recibe (props):
 * - className: (string) → clases adicionales de CSS para personalizar estilos.
 * - ...props: cualquier otra prop válida de `LabelPrimitive.Root`, por ejemplo:
 *    - `htmlFor`: id del campo de formulario al que está vinculado.
 *    - `children`: el texto o contenido que se muestra como etiqueta.
 *
 * ⚠️ ¿Qué pasa si no pasamos todas las props?
 * - `children` (el texto de la etiqueta) → si no lo pasamos, el label no muestra nada visible.
 * - `htmlFor` → si no lo pasamos, el label no quedará vinculado a ningún input,
 *   lo cual rompe parcialmente la accesibilidad (aunque seguirá renderizando el texto).
 * - `className` → es opcional, sin pasarlo se aplican solo los estilos base definidos.
 */
function Label({ className, ...props }) {
  return (
    <LabelPrimitive.Root
      data-slot="label"
      className={cn(
        // Estilos base: tipografía y alineación
        "flex items-center gap-2 text-sm leading-none font-medium select-none",
        // Estados de accesibilidad (ej: inputs deshabilitados)
        "group-data-[disabled=true]:pointer-events-none group-data-[disabled=true]:opacity-50",
        "peer-disabled:cursor-not-allowed peer-disabled:opacity-50",
        className
      )}
      {...props}
    />
  );
}

/* 🔹 Exportación */
export { Label };
