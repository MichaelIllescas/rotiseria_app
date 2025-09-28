"use client";

import * as React from "react";
import { GripVerticalIcon } from "lucide-react";
import * as ResizablePrimitive from "react-resizable-panels";
import { cn } from "./utils";

/**
 * 📌 ResizablePanelGroup
 *
 * Contenedor principal que agrupa varios `ResizablePanel`.
 * Controla la orientación (horizontal o vertical) y el layout de los paneles.
 *
 * ✅ Props (hereda de `ResizablePrimitive.PanelGroup`):
 * - direction: "horizontal" | "vertical" → orientación de los paneles (OBLIGATORIO).
 * - className?: estilos personalizados.
 * - children: deben ser `ResizablePanel` + `ResizableHandle`.
 *
 * ⚠️ Si no se pasa `direction`, no se define la orientación → puede romper el layout.
 */
function ResizablePanelGroup({ className, ...props }) {
  return (
    <ResizablePrimitive.PanelGroup
      data-slot="resizable-panel-group"
      className={cn(
        "flex h-full w-full data-[panel-group-direction=vertical]:flex-col", // si es vertical cambia a columnas
        className,
      )}
      {...props}
    />
  );
}

/**
 * 📌 ResizablePanel
 *
 * Panel individual dentro de un `ResizablePanelGroup`.
 * Su tamaño puede ser fijo, dinámico o ajustable.
 *
 * ✅ Props (hereda de `ResizablePrimitive.Panel`):
 * - defaultSize?: número → tamaño inicial en porcentaje.
 * - minSize?: número → tamaño mínimo.
 * - maxSize?: número → tamaño máximo.
 * - collapsible?: boolean → si puede colapsarse.
 *
 * ⚠️ Si no le pasás tamaños (`defaultSize`/`minSize`/`maxSize`), se ajusta automáticamente.
 */
function ResizablePanel({ ...props }) {
  return <ResizablePrimitive.Panel data-slot="resizable-panel" {...props} />;
}

/**
 * 📌 ResizableHandle
 *
 * Separador que permite al usuario redimensionar los paneles arrastrando.
 * Puede incluir un "handler" visual (icono) si `withHandle` es true.
 *
 * ✅ Props (hereda de `ResizablePrimitive.PanelResizeHandle`):
 * - withHandle?: boolean → si muestra el ícono de "agarre".
 * - className?: estilos personalizados.
 *
 * ⚠️ Si no se pasa `withHandle`, solo se muestra la línea separadora.
 */
function ResizableHandle({ withHandle, className, ...props }) {
  return (
    <ResizablePrimitive.PanelResizeHandle
      data-slot="resizable-handle"
      className={cn(
        "bg-border focus-visible:ring-ring relative flex w-px items-center justify-center \
         after:absolute after:inset-y-0 after:left-1/2 after:w-1 after:-translate-x-1/2 \
         focus-visible:ring-1 focus-visible:ring-offset-1 focus-visible:outline-hidden \
         data-[panel-group-direction=vertical]:h-px \
         data-[panel-group-direction=vertical]:w-full \
         data-[panel-group-direction=vertical]:after:left-0 \
         data-[panel-group-direction=vertical]:after:h-1 \
         data-[panel-group-direction=vertical]:after:w-full \
         data-[panel-group-direction=vertical]:after:-translate-y-1/2 \
         data-[panel-group-direction=vertical]:after:translate-x-0 \
         [&[data-panel-group-direction=vertical]>div]:rotate-90",
        className,
      )}
      {...props}
    >
      {withHandle && (
        <div className="bg-border z-10 flex h-4 w-3 items-center justify-center rounded-xs border">
          <GripVerticalIcon className="size-2.5" />
        </div>
      )}
    </ResizablePrimitive.PanelResizeHandle>
  );
}

/* 🔹 Exportación */
export { ResizablePanelGroup, ResizablePanel, ResizableHandle };
