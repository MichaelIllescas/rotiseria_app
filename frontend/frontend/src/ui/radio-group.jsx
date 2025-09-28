"use client";

import * as React from "react";
import * as RadioGroupPrimitive from "@radix-ui/react-radio-group";
import { CircleIcon } from "lucide-react";
import { cn } from "./utils";

/**
 * 📌 RadioGroup
 *
 * Contenedor que agrupa varios `RadioGroupItem`. 
 * Permite seleccionar **una sola opción** dentro de un grupo.
 *
 * ✅ Props (hereda de `RadioGroupPrimitive.Root`):
 * - value?: string → valor seleccionado.
 * - defaultValue?: string → valor inicial.
 * - onValueChange?: (value: string) => void → callback al cambiar selección.
 * - className?: estilos personalizados.
 *
 * ⚠️ Si no se pasa `value` o `defaultValue`, el grupo inicia sin opción seleccionada.
 */
function RadioGroup({ className, ...props }) {
  return (
    <RadioGroupPrimitive.Root
      data-slot="radio-group"
      className={cn("grid gap-3", className)}
      {...props}
    />
  );
}

/**
 * 📌 RadioGroupItem
 *
 * Opción individual dentro de un `RadioGroup`. 
 * Se representa como un botón circular (radio button).
 *
 * ✅ Props (hereda de `RadioGroupPrimitive.Item`):
 * - value: string → valor único de este item (OBLIGATORIO para diferenciarlo).
 * - disabled?: boolean → deshabilita la opción.
 * - className?: estilos personalizados.
 *
 * ⚠️ Si no se pasa `value`, el item no puede ser identificado dentro del grupo → no funcionará bien.
 */
function RadioGroupItem({ className, ...props }) {
  return (
    <RadioGroupPrimitive.Item
      data-slot="radio-group-item"
      className={cn(
        "border-input text-primary focus-visible:border-ring focus-visible:ring-ring/50 \
         aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 \
         aria-invalid:border-destructive dark:bg-input/30 aspect-square size-4 shrink-0 \
         rounded-full border shadow-xs transition-[color,box-shadow] outline-none \
         focus-visible:ring-[3px] disabled:cursor-not-allowed disabled:opacity-50",
        className,
      )}
      {...props}
    >
      {/* Indicador visible cuando el ítem está seleccionado */}
      <RadioGroupPrimitive.Indicator
        data-slot="radio-group-indicator"
        className="relative flex items-center justify-center"
      >
        <CircleIcon className="fill-primary absolute top-1/2 left-1/2 size-2 -translate-x-1/2 -translate-y-1/2" />
      </RadioGroupPrimitive.Indicator>
    </RadioGroupPrimitive.Item>
  );
}

/* 🔹 Exportación */
export { RadioGroup, RadioGroupItem };
