"use client";

import * as React from "react";
import * as SliderPrimitive from "@radix-ui/react-slider";
import { cn } from "./utils";

/**
 * 📌 Slider
 *
 * Barra deslizante para seleccionar un valor o rango.
 * Basado en Radix UI → garantiza accesibilidad y estados controlados.
 *
 * ✅ Props principales:
 * - value: número | número[] → valor actual (modo controlado).
 * - defaultValue: número[] → valor inicial (modo no controlado).
 * - min: número → valor mínimo (default: 0).
 * - max: número → valor máximo (default: 100).
 * - className: string → clases CSS extra.
 *
 * ⚠️ Importante:
 * - Si `value` y `defaultValue` no se pasan → el slider va de [min, max].
 */
function Slider({ className, defaultValue, value, min = 0, max = 100, ...props }) {
  /**
   * 🧮 _values
   * - Se calcula con `useMemo` para determinar cuántos *thumbs* (manijas) mostrar.
   * - Si `value` es un array → lo usamos.
   * - Si no, revisamos `defaultValue`.
   * - Si ninguno está definido → usamos [min, max].
   */
  const _values = React.useMemo(
    () =>
      Array.isArray(value)
        ? value
        : Array.isArray(defaultValue)
        ? defaultValue
        : [min, max],
    [value, defaultValue, min, max]
  );

  return (
    <SliderPrimitive.Root
      data-slot="slider"
      defaultValue={defaultValue}
      value={value}
      min={min}
      max={max}
      // Estilos base + variantes según orientación (horizontal/vertical)
      className={cn(
        "relative flex w-full touch-none items-center select-none data-[disabled]:opacity-50",
        "data-[orientation=vertical]:h-full data-[orientation=vertical]:min-h-44",
        "data-[orientation=vertical]:w-auto data-[orientation=vertical]:flex-col",
        className
      )}
      {...props}
    >
      {/* Track → la barra completa */}
      <SliderPrimitive.Track
        data-slot="slider-track"
        className="bg-muted relative grow overflow-hidden rounded-full data-[orientation=horizontal]:h-4 data-[orientation=horizontal]:w-full data-[orientation=vertical]:h-full data-[orientation=vertical]:w-1.5"
      >
        {/* Range → parte coloreada según el valor */}
        <SliderPrimitive.Range
          data-slot="slider-range"
          className="bg-primary absolute data-[orientation=horizontal]:h-full data-[orientation=vertical]:w-full"
        />
      </SliderPrimitive.Track>

      {/* Thumbs → los círculos que el usuario arrastra */}
      {Array.from({ length: _values.length }, (_, index) => (
        <SliderPrimitive.Thumb
          key={index}
          data-slot="slider-thumb"
          className="border-primary bg-background ring-ring/50 block size-4 shrink-0 rounded-full border shadow-sm transition-[color,box-shadow] hover:ring-4 focus-visible:ring-4 focus-visible:outline-hidden disabled:pointer-events-none disabled:opacity-50"
        />
      ))}
    </SliderPrimitive.Root>
  );
}

export { Slider };
