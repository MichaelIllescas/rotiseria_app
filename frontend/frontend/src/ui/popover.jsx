"use client";

import * as React from "react";
import * as PopoverPrimitive from "@radix-ui/react-popover";
import { cn } from "./utils";

/**
 * 📌 Popover
 *
 * Contenedor principal que maneja el estado de apertura/cierre del Popover.
 *
 * ✅ Props:
 * - Cualquier prop de `PopoverPrimitive.Root` (ej: open, defaultOpen, onOpenChange).
 *
 * ⚠️ Si no se pasa `children`, el Popover no renderiza nada.
 */
function Popover({ ...props }) {
  return <PopoverPrimitive.Root data-slot="popover" {...props} />;
}

/**
 * 📌 PopoverTrigger
 *
 * Elemento que dispara la apertura del Popover (ej: un botón o link).
 *
 * ✅ Props:
 * - Cualquier prop de `PopoverPrimitive.Trigger`.
 *
 * ⚠️ Si no se incluye, el Popover no tendrá forma de abrirse.
 */
function PopoverTrigger({ ...props }) {
  return <PopoverPrimitive.Trigger data-slot="popover-trigger" {...props} />;
}

/**
 * 📌 PopoverContent
 *
 * Contenido que se muestra cuando el Popover está abierto.
 * Se renderiza dentro de un Portal para que no quede limitado por `overflow:hidden`.
 *
 * ✅ Props:
 * - className: estilos personalizados.
 * - align: posición horizontal relativa al trigger (default: "center").
 * - sideOffset: separación respecto al trigger (default: 4px).
 * - Cualquier prop de `PopoverPrimitive.Content`.
 *
 * ⚠️ Si no se pasa contenido, el Popover abre vacío.
 */
function PopoverContent({
  className,
  align = "center",
  sideOffset = 4,
  ...props
}) {
  return (
    <PopoverPrimitive.Portal>
      <PopoverPrimitive.Content
        data-slot="popover-content"
        align={align}
        sideOffset={sideOffset}
        className={cn(
          "bg-popover text-popover-foreground data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 data-[state=closed]:zoom-out-95 data-[state=open]:zoom-in-95 data-[side=bottom]:slide-in-from-top-2 data-[side=left]:slide-in-from-right-2 data-[side=right]:slide-in-from-left-2 data-[side=top]:slide-in-from-bottom-2 z-50 w-72 origin-(--radix-popover-content-transform-origin) rounded-md border p-4 shadow-md outline-hidden",
          className
        )}
        {...props}
      />
    </PopoverPrimitive.Portal>
  );
}

/**
 * 📌 PopoverAnchor
 *
 * Permite posicionar el Popover en relación a un elemento específico
 * distinto del `Trigger`.
 *
 * ✅ Props:
 * - Cualquier prop de `PopoverPrimitive.Anchor`.
 *
 * ⚠️ Si no se usa, el Popover se posiciona automáticamente en relación al `Trigger`.
 */
function PopoverAnchor({ ...props }) {
  return <PopoverPrimitive.Anchor data-slot="popover-anchor" {...props} />;
}

/* 🔹 Exportación */
export { Popover, PopoverTrigger, PopoverContent, PopoverAnchor };
