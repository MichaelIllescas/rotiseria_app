"use client";

import * as React from "react";
import * as SwitchPrimitive from "@radix-ui/react-switch";
import { cn } from "./utils";

/**
 * 📌 Switch
 *
 * Componente de interruptor (toggle) accesible basado en Radix UI.
 * Se utiliza para alternar entre dos estados (ej: on/off, activo/inactivo).
 *
 * ✅ Props:
 * - checked?: boolean → controla el estado del switch (ON/OFF).
 * - defaultChecked?: boolean → valor inicial si no se controla externamente.
 * - onCheckedChange?: (checked: boolean) => void → callback al cambiar de estado.
 * - disabled?: boolean → deshabilita la interacción.
 * - className?: string → clases adicionales para personalizar estilos.
 *
 * 🔹 Funcionamiento:
 * - Usa `SwitchPrimitive.Root` como contenedor del switch.
 * - Dentro, `SwitchPrimitive.Thumb` representa el círculo móvil.
 * - Cambia estilos según el estado `data-[state=checked]` o `data-[state=unchecked]`.
 *
 * ⚠️ Notas:
 * - Debe usarse con un `label` accesible para cumplir con buenas prácticas de accesibilidad.
 * - Si no se pasa `checked` ni `defaultChecked`, inicia apagado (unchecked).
 */
function Switch({ className, ...props }) {
  return (
    <SwitchPrimitive.Root
      data-slot="switch"
      className={cn(
        // Estilos base del switch
        "peer inline-flex h-[1.15rem] w-8 shrink-0 items-center rounded-full border border-transparent transition-all outline-none",
        // Estado cuando está activado (ON)
        "data-[state=checked]:bg-primary",
        // Estado cuando está apagado (OFF)
        "data-[state=unchecked]:bg-switch-background dark:data-[state=unchecked]:bg-input/80",
        // Estilos de enfoque
        "focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px]",
        // Estado deshabilitado
        "disabled:cursor-not-allowed disabled:opacity-50",
        className
      )}
      {...props}
    >
      <SwitchPrimitive.Thumb
        data-slot="switch-thumb"
        className={cn(
          // Estilos base del "pulgar" (círculo móvil)
          "pointer-events-none block size-4 rounded-full ring-0 transition-transform bg-card",
          // Estado ON → el pulgar se mueve a la derecha
          "data-[state=checked]:translate-x-[calc(100%-2px)] dark:data-[state=checked]:bg-primary-foreground",
          // Estado OFF → el pulgar se queda a la izquierda
          "data-[state=unchecked]:translate-x-0 dark:data-[state=unchecked]:bg-card-foreground"
        )}
      />
    </SwitchPrimitive.Root>
  );
}

/* 🔹 Exportación */
export { Switch };
