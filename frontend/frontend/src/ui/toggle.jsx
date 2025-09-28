"use client";

import * as React from "react";
import * as TogglePrimitive from "@radix-ui/react-toggle";
import { cva } from "class-variance-authority";
import { cn } from "./utils";

/**
 * 🎨 toggleVariants
 *
 * Utilizamos `cva` (Class Variance Authority) para definir
 * un sistema de variantes reutilizable:
 *
 * - variant:
 *   - "default" → estilo base sin bordes, fondo transparente.
 *   - "outline" → con borde + fondo transparente (resalta al hover).
 *
 * - size:
 *   - "default" → altura 9, padding 2, ancho mínimo 9.
 *   - "sm" → más chico (altura 8, padding 1.5).
 *   - "lg" → más grande (altura 10, padding 2.5).
 *
 * - defaultVariants → si no se especifica, usa "default" tanto en variant como en size.
 */
const toggleVariants = cva(
  "inline-flex items-center justify-center gap-2 rounded-md text-sm font-medium whitespace-nowrap \
   hover:bg-muted hover:text-muted-foreground \
   disabled:pointer-events-none disabled:opacity-50 \
   data-[state=on]:bg-accent data-[state=on]:text-accent-foreground \
   [&_svg]:pointer-events-none [&_svg:not([class*='size-'])]:size-4 [&_svg]:shrink-0 \
   focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] outline-none \
   transition-[color,box-shadow] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive",
  {
    variants: {
      variant: {
        default: "bg-transparent",
        outline:
          "border border-input bg-transparent hover:bg-accent hover:text-accent-foreground",
      },
      size: {
        default: "h-9 px-2 min-w-9",
        sm: "h-8 px-1.5 min-w-8",
        lg: "h-10 px-2.5 min-w-10",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  }
);

/**
 * 📌 Toggle
 *
 * Botón de tipo "switch on/off" con estilos personalizables.
 * Se basa en `@radix-ui/react-toggle` para accesibilidad y manejo de estado.
 *
 * ✅ Props:
 * - variant?: "default" | "outline" → estilo visual.
 * - size?: "sm" | "default" | "lg" → tamaño del botón.
 * - className?: string → estilos extra personalizados.
 * - ...props → cualquier prop válida de `TogglePrimitive.Root`.
 *
 * ⚠️ Si no se pasa `variant` o `size`, se aplican los valores por defecto.
 */
function Toggle({ className, variant, size, ...props }) {
  return (
    <TogglePrimitive.Root
      data-slot="toggle"
      className={cn(toggleVariants({ variant, size, className }))}
      {...props}
    />
  );
}

export { Toggle, toggleVariants };
