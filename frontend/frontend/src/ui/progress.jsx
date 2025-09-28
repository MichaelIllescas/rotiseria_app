"use client";

import * as React from "react";
import * as ProgressPrimitive from "@radix-ui/react-progress";
import { cn } from "./utils";

/**
 * 📌 Progress
 *
 * Barra de progreso para mostrar visualmente el avance de una tarea 
 * (ejemplo: carga de archivo, porcentaje completado).
 *
 * ✅ Props (hereda de `ProgressPrimitive.Root`):
 * - value?: número entre 0 y 100 → indica el progreso actual.
 * - className?: estilos personalizados.
 * - ...props: cualquier otra prop válida de un <div>.
 *
 * ⚠️ Si no se pasa `value`, se asume 0 (barra vacía).
 */
function Progress({ className, value, ...props }) {
  return (
    <ProgressPrimitive.Root
      data-slot="progress"
      className={cn(
        "bg-primary/20 relative h-2 w-full overflow-hidden rounded-full",
        className,
      )}
      {...props}
    >
      {/* Indicador interno que se desplaza según el valor */}
      <ProgressPrimitive.Indicator
        data-slot="progress-indicator"
        className="bg-primary h-full w-full flex-1 transition-all"
        // Calcula el ancho visible según el porcentaje (value)
        style={{ transform: `translateX(-${100 - (value || 0)}%)` }}
      />
    </ProgressPrimitive.Root>
  );
}

/* 🔹 Exportación */
export { Progress };
