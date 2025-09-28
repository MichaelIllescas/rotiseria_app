"use client";

import * as React from "react";
import * as ScrollAreaPrimitive from "@radix-ui/react-scroll-area";
import { cn } from "./utils";

/**
 * 📌 ScrollArea
 *
 * Contenedor con scroll personalizado usando Radix.
 * Sirve para envolver contenido que puede desbordarse en vertical/horizontal
 * y mostrar barras de desplazamiento estilizadas en lugar de las nativas.
 *
 * ✅ Props (hereda de `ScrollAreaPrimitive.Root`):
 * - className?: estilos adicionales.
 * - children: contenido a mostrar dentro del área con scroll.
 *
 * ⚠️ Si no se pasan props, funciona igual, pero usará los estilos por defecto.
 */
function ScrollArea({ className, children, ...props }) {
  return (
    <ScrollAreaPrimitive.Root
      data-slot="scroll-area"
      className={cn("relative", className)}
      {...props}
    >
      {/* Viewport: área visible del scroll */}
      <ScrollAreaPrimitive.Viewport
        data-slot="scroll-area-viewport"
        className="focus-visible:ring-ring/50 size-full rounded-[inherit] transition-[color,box-shadow] outline-none focus-visible:ring-[3px] focus-visible:outline-1"
      >
        {children}
      </ScrollAreaPrimitive.Viewport>

      {/* Barra de scroll personalizada */}
      <ScrollBar />

      {/* Esquina donde se cruzan scroll vertical y horizontal */}
      <ScrollAreaPrimitive.Corner />
    </ScrollAreaPrimitive.Root>
  );
}

/**
 * 📌 ScrollBar
 *
 * Barra de desplazamiento personalizada para `ScrollArea`.
 * Puede ser vertical u horizontal, según la orientación que se pase.
 *
 * ✅ Props (hereda de `ScrollAreaPrimitive.ScrollAreaScrollbar`):
 * - orientation?: "vertical" | "horizontal" (por defecto "vertical").
 * - className?: estilos adicionales.
 *
 * ⚠️ Si no se pasa `orientation`, siempre será vertical.
 */
function ScrollBar({ className, orientation = "vertical", ...props }) {
  return (
    <ScrollAreaPrimitive.ScrollAreaScrollbar
      data-slot="scroll-area-scrollbar"
      orientation={orientation}
      className={cn(
        "flex touch-none p-px transition-colors select-none",
        orientation === "vertical" &&
          "h-full w-2.5 border-l border-l-transparent",
        orientation === "horizontal" &&
          "h-2.5 flex-col border-t border-t-transparent",
        className,
      )}
      {...props}
    >
      {/* Thumb: parte "arrastrable" de la barra */}
      <ScrollAreaPrimitive.ScrollAreaThumb
        data-slot="scroll-area-thumb"
        className="bg-border relative flex-1 rounded-full"
      />
    </ScrollAreaPrimitive.ScrollAreaScrollbar>
  );
}

/* 🔹 Exportación */
export { ScrollArea, ScrollBar };
