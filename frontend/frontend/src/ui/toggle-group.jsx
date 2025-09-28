"use client";

import * as React from "react";
import * as ToggleGroupPrimitive from "@radix-ui/react-toggle-group";
import { cn } from "./utils";
import { toggleVariants } from "./toggle";

/**
 * 📌 Contexto para compartir estilos (variant y size)
 * entre los ítems del grupo.
 * 
 * - `variant`: estilo visual (ej: default, outline).
 * - `size`: tamaño de los botones (ej: sm, default, lg).
 */
const ToggleGroupContext = React.createContext({
  size: "default",
  variant: "default",
});

/**
 * 📌 ToggleGroup
 *
 * Contenedor principal que agrupa varios botones de tipo `ToggleGroupItem`.
 * Permite seleccionar uno o varios (según configuración).
 *
 * ✅ Props (hereda de `ToggleGroupPrimitive.Root`):
 * - type: "single" | "multiple" → define si se puede seleccionar uno o varios.
 * - value?: string | string[] → valor/es seleccionados.
 * - defaultValue?: string | string[] → valor/es iniciales.
 * - onValueChange?: callback cuando cambia la selección.
 * - variant?: estilo (ej: "default" o "outline").
 * - size?: tamaño de los botones.
 * - className?: estilos personalizados.
 *
 * ⚠️ Si no se pasa `type`, por defecto se comporta como selección múltiple.
 */
function ToggleGroup({ className, variant, size, children, ...props }) {
  return (
    <ToggleGroupPrimitive.Root
      data-slot="toggle-group"
      data-variant={variant}
      data-size={size}
      className={cn(
        "group/toggle-group flex w-fit items-center rounded-md data-[variant=outline]:shadow-xs",
        className
      )}
      {...props}
    >
      {/* Contexto para que todos los ítems compartan variant y size */}
      <ToggleGroupContext.Provider value={{ variant, size }}>
        {children}
      </ToggleGroupContext.Provider>
    </ToggleGroupPrimitive.Root>
  );
}

/**
 * 📌 ToggleGroupItem
 *
 * Botón dentro del `ToggleGroup`. 
 * Puede estar activado o desactivado según el valor actual.
 *
 * ✅ Props (hereda de `ToggleGroupPrimitive.Item`):
 * - value: string → valor único de este item (OBLIGATORIO).
 * - disabled?: boolean → deshabilita el botón.
 * - variant?: estilo visual (hereda del contexto si no se pasa).
 * - size?: tamaño (hereda del contexto si no se pasa).
 * - children: contenido (ej: texto o ícono).
 *
 * 🎨 Estilos:
 * - Se aplican con `toggleVariants` para mantener consistencia.
 * - Bordes redondeados solo en extremos (first y last).
 */
function ToggleGroupItem({ className, children, variant, size, ...props }) {
  const context = React.useContext(ToggleGroupContext);

  return (
    <ToggleGroupPrimitive.Item
      data-slot="toggle-group-item"
      data-variant={context.variant || variant}
      data-size={context.size || size}
      className={cn(
        toggleVariants({
          variant: context.variant || variant,
          size: context.size || size,
        }),
        "min-w-0 flex-1 shrink-0 rounded-none shadow-none first:rounded-l-md last:rounded-r-md focus:z-10 focus-visible:z-10 data-[variant=outline]:border-l-0 data-[variant=outline]:first:border-l",
        className
      )}
      {...props}
    >
      {children}
    </ToggleGroupPrimitive.Item>
  );
}

export { ToggleGroup, ToggleGroupItem };
