"use client";

import * as React from "react";
import * as TooltipPrimitive from "@radix-ui/react-tooltip";
import { cn } from "./utils";

/**
 * 📌 TooltipProvider
 *
 * Proveedor global de tooltips.
 * - Se encarga de manejar el tiempo de delay antes de mostrar los tooltips.
 *
 * ✅ Props:
 * - delayDuration?: número en ms (default: 0) → tiempo de espera antes de mostrar el tooltip.
 * - ...props: todas las props de `TooltipPrimitive.Provider`.
 *
 * ⚠️ Si no lo usás, los tooltips no tendrán control de delay global.
 */
function TooltipProvider({ delayDuration = 0, ...props }) {
  return (
    <TooltipPrimitive.Provider
      data-slot="tooltip-provider"
      delayDuration={delayDuration}
      {...props}
    />
  );
}

/**
 * 📌 Tooltip
 *
 * Componente raíz del tooltip. Encapsula el `Root` de Radix.
 * - Se recomienda envolver cada tooltip con este componente.
 *
 * ✅ Props:
 * - open?: boolean → controla si el tooltip está visible (modo controlado).
 * - defaultOpen?: boolean → abre el tooltip inicialmente.
 * - onOpenChange?: función → callback al abrir/cerrar.
 * - children: `TooltipTrigger` + `TooltipContent`.
 */
function Tooltip({ ...props }) {
  return (
    <TooltipProvider>
      <TooltipPrimitive.Root data-slot="tooltip" {...props} />
    </TooltipProvider>
  );
}

/**
 * 📌 TooltipTrigger
 *
 * Elemento que dispara la apertura del tooltip.
 * Generalmente un botón, icono o link.
 *
 * ✅ Props:
 * - asChild?: boolean → permite pasar un componente custom sin alterar su DOM.
 * - ...props: cualquier prop de `TooltipPrimitive.Trigger`.
 *
 * ⚠️ Si no se incluye, el tooltip no tiene "ancla" y nunca se abre.
 */
function TooltipTrigger({ ...props }) {
  return <TooltipPrimitive.Trigger data-slot="tooltip-trigger" {...props} />;
}

/**
 * 📌 TooltipContent
 *
 * Contenido que se muestra en el tooltip cuando está abierto.
 *
 * ✅ Props:
 * - className?: estilos adicionales.
 * - side?: "top" | "bottom" | "left" | "right" (default Radix: "top").
 * - sideOffset?: número → separación respecto al trigger (default: 0).
 * - children: contenido visible dentro del tooltip.
 *
 * ⚠️ Si no se pasa `children`, el tooltip abrirá vacío.
 */
function TooltipContent({ className, sideOffset = 0, children, ...props }) {
  return (
    <TooltipPrimitive.Portal>
      <TooltipPrimitive.Content
        data-slot="tooltip-content"
        sideOffset={sideOffset}
        className={cn(
          "bg-primary text-primary-foreground animate-in fade-in-0 zoom-in-95 \
           data-[state=closed]:animate-out data-[state=closed]:fade-out-0 \
           data-[state=closed]:zoom-out-95 \
           data-[side=bottom]:slide-in-from-top-2 \
           data-[side=left]:slide-in-from-right-2 \
           data-[side=right]:slide-in-from-left-2 \
           data-[side=top]:slide-in-from-bottom-2 \
           z-50 w-fit origin-(--radix-tooltip-content-transform-origin) \
           rounded-md px-3 py-1.5 text-xs text-balance",
          className
        )}
        {...props}
      >
        {children}
        {/* Flechita decorativa que apunta al trigger */}
        <TooltipPrimitive.Arrow className="bg-primary fill-primary z-50 size-2.5 translate-y-[calc(-50%_-_2px)] rotate-45 rounded-[2px]" />
      </TooltipPrimitive.Content>
    </TooltipPrimitive.Portal>
  );
}

export { Tooltip, TooltipTrigger, TooltipContent, TooltipProvider };
