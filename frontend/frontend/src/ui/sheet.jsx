"use client";

import * as React from "react";
import * as SheetPrimitive from "@radix-ui/react-dialog";
import { XIcon } from "lucide-react";

import { cn } from "./utils";

/**
 * 📌 Sheet
 *
 * Componente tipo "drawer" (panel deslizante).
 * Se abre desde un lado de la pantalla (right, left, top, bottom).
 * Está basado en `@radix-ui/react-dialog` para accesibilidad y control de estados.
 *
 * 👉 Útil para:
 * - Mostrar menús laterales.
 * - Formularios de configuración rápida.
 * - Contenido secundario sin abandonar la vista principal.
 */

/**
 * ✅ Root principal que controla el estado del Sheet (open/close).
 */
function Sheet(props) {
  return <SheetPrimitive.Root data-slot="sheet" {...props} />;
}

/**
 * ✅ Trigger
 * - Botón o elemento que abre el Sheet.
 */
function SheetTrigger(props) {
  return <SheetPrimitive.Trigger data-slot="sheet-trigger" {...props} />;
}

/**
 * ✅ Close
 * - Elemento para cerrar el Sheet manualmente.
 */
function SheetClose(props) {
  return <SheetPrimitive.Close data-slot="sheet-close" {...props} />;
}

/**
 * ✅ Portal
 * - Renderiza el Sheet en un portal, fuera del flujo principal del DOM,
 *   ideal para overlays.
 */
function SheetPortal(props) {
  return <SheetPrimitive.Portal data-slot="sheet-portal" {...props} />;
}

/**
 * ✅ Overlay
 * - Fondo oscuro detrás del Sheet.
 * - Bloquea interacción con el contenido detrás.
 */
function SheetOverlay({ className, ...props }) {
  return (
    <SheetPrimitive.Overlay
      data-slot="sheet-overlay"
      className={cn(
        "data-[state=open]:animate-in data-[state=closed]:animate-out " +
          "data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 " +
          "fixed inset-0 z-50 bg-black/50",
        className
      )}
      {...props}
    />
  );
}

/**
 * ✅ Content
 * - El panel deslizante en sí.
 * - `side` define desde qué lado aparece (right | left | top | bottom).
 * - Incluye botón de cierre en la esquina superior derecha.
 */
function SheetContent({ className, children, side = "right", ...props }) {
  return (
    <SheetPortal>
      <SheetOverlay />
      <SheetPrimitive.Content
        data-slot="sheet-content"
        className={cn(
          "bg-background data-[state=open]:animate-in data-[state=closed]:animate-out " +
            "fixed z-50 flex flex-col gap-4 shadow-lg transition ease-in-out " +
            "data-[state=closed]:duration-300 data-[state=open]:duration-500",
          side === "right" &&
            "data-[state=closed]:slide-out-to-right data-[state=open]:slide-in-from-right inset-y-0 right-0 h-full w-3/4 border-l sm:max-w-sm",
          side === "left" &&
            "data-[state=closed]:slide-out-to-left data-[state=open]:slide-in-from-left inset-y-0 left-0 h-full w-3/4 border-r sm:max-w-sm",
          side === "top" &&
            "data-[state=closed]:slide-out-to-top data-[state=open]:slide-in-from-top inset-x-0 top-0 h-auto border-b",
          side === "bottom" &&
            "data-[state=closed]:slide-out-to-bottom data-[state=open]:slide-in-from-bottom inset-x-0 bottom-0 h-auto border-t",
          className
        )}
        {...props}
      >
        {children}
        {/* Botón de cierre */}
        <SheetPrimitive.Close className="ring-offset-background focus:ring-ring data-[state=open]:bg-secondary absolute top-4 right-4 rounded-xs opacity-70 transition-opacity hover:opacity-100 focus:ring-2 focus:ring-offset-2 focus:outline-hidden disabled:pointer-events-none">
          <XIcon className="size-4" />
          <span className="sr-only">Close</span>
        </SheetPrimitive.Close>
      </SheetPrimitive.Content>
    </SheetPortal>
  );
}

/**
 * ✅ Header
 * - Contenedor para el encabezado del Sheet.
 * - Generalmente usado para título y subtítulo.
 */
function SheetHeader({ className, ...props }) {
  return (
    <div
      data-slot="sheet-header"
      className={cn("flex flex-col gap-1.5 p-4", className)}
      {...props}
    />
  );
}

/**
 * ✅ Footer
 * - Contenedor para acciones (botones, links, etc.) en la parte inferior del Sheet.
 */
function SheetFooter({ className, ...props }) {
  return (
    <div
      data-slot="sheet-footer"
      className={cn("mt-auto flex flex-col gap-2 p-4", className)}
      {...props}
    />
  );
}

/**
 * ✅ Title
 * - Título principal del Sheet.
 */
function SheetTitle({ className, ...props }) {
  return (
    <SheetPrimitive.Title
      data-slot="sheet-title"
      className={cn("text-foreground font-semibold", className)}
      {...props}
    />
  );
}

/**
 * ✅ Description
 * - Texto secundario para describir el propósito del Sheet.
 */
function SheetDescription({ className, ...props }) {
  return (
    <SheetPrimitive.Description
      data-slot="sheet-description"
      className={cn("text-muted-foreground text-sm", className)}
      {...props}
    />
  );
}

/* 🔹 Exportaciones */
export {
  Sheet,
  SheetTrigger,
  SheetClose,
  SheetContent,
  SheetHeader,
  SheetFooter,
  SheetTitle,
  SheetDescription,
};
