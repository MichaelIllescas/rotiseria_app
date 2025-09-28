"use client";

import * as React from "react";
import * as SeparatorPrimitive from "@radix-ui/react-separator";

import { cn } from "./utils";

/**
 * 📌 Separator
 *
 * Componente usado para dividir contenido visual o semánticamente.
 * Puede ser una línea horizontal o vertical según la orientación.
 *
 * 👉 Útil para:
 * - Separar secciones en formularios, menús o paneles.
 * - Mejorar la legibilidad visual.
 * - Proveer estructura semántica (si `decorative=false`, es reconocido por accesibilidad).
 *
 * ✅ Props:
 * - orientation?: "horizontal" | "vertical" (default: "horizontal") → define la dirección de la línea.
 * - decorative?: boolean (default: true) → indica si es solo decorativo o semántico.
 * - className?: string → permite añadir estilos adicionales con Tailwind o CSS propio.
 * - ...props: cualquier otra prop soportada por `<div>` o Radix Separator.
 *
 * 🔹 Comportamiento si no se pasan props:
 * - Se renderiza como un separador horizontal (`orientation="horizontal"`) y decorativo (`decorative=true`).
 * - Aparece como una línea horizontal simple.
 */
function Separator({ className, orientation = "horizontal", decorative = true, ...props }) {
  return (
    <SeparatorPrimitive.Root
      data-slot="separator-root" // atributo útil para debug/testing
      decorative={decorative}
      orientation={orientation}
      className={cn(
        // Estilos base: cambia según orientación
        "bg-border shrink-0 " +
          "data-[orientation=horizontal]:h-px data-[orientation=horizontal]:w-full " +
          "data-[orientation=vertical]:h-full data-[orientation=vertical]:w-px",
        className
      )}
      {...props}
    />
  );
}

/* 🔹 Exportación */
export { Separator };
