"use client";

import * as React from "react";
import * as TabsPrimitive from "@radix-ui/react-tabs";
import { cn } from "./utils";

/**
 * 📌 Tabs
 *
 * Contenedor principal de un grupo de pestañas.
 * Gestiona el estado (qué tab está activa) y muestra su contenido asociado.
 *
 * ✅ Props (heredadas de `TabsPrimitive.Root`):
 * - defaultValue: string → valor de la pestaña activa por defecto.
 * - value: string → valor controlado de la pestaña activa.
 * - onValueChange: (value: string) => void → callback al cambiar de tab.
 * - className: string → clases personalizadas.
 *
 * ⚠️ Si no se pasan `defaultValue` o `value`, el componente no sabrá
 * qué tab mostrar al inicio → quedará vacío hasta que se seleccione.
 */
function Tabs({ className, ...props }) {
  return (
    <TabsPrimitive.Root
      data-slot="tabs"
      className={cn("flex flex-col gap-2", className)}
      {...props}
    />
  );
}

/**
 * 📌 TabsList
 *
 * Contenedor para los botones de pestaña (Triggers).
 * Normalmente se coloca arriba o a la izquierda de los contenidos.
 *
 * ✅ Props:
 * - className: string → estilos adicionales.
 *
 * ⚠️ Si no contiene `TabsTrigger` como children, no se mostrará nada.
 */
function TabsList({ className, ...props }) {
  return (
    <TabsPrimitive.List
      data-slot="tabs-list"
      className={cn(
        "bg-muted text-muted-foreground inline-flex h-9 w-fit items-center justify-center rounded-xl p-[3px] flex",
        className
      )}
      {...props}
    />
  );
}

/**
 * 📌 TabsTrigger
 *
 * Botón que representa una pestaña.
 * Cuando se hace clic, activa el contenido asociado con su `value`.
 *
 * ✅ Props (heredadas de `TabsPrimitive.Trigger`):
 * - value: string → valor único de la pestaña (OBLIGATORIO).
 * - disabled?: boolean → desactiva la pestaña.
 * - className: string → estilos personalizados.
 *
 * ⚠️ Si no se pasa `value`, la pestaña no funcionará porque no tendrá
 * qué contenido asociar.
 */
function TabsTrigger({ className, ...props }) {
  return (
    <TabsPrimitive.Trigger
      data-slot="tabs-trigger"
      className={cn(
        "data-[state=active]:bg-card dark:data-[state=active]:text-foreground focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:outline-ring dark:data-[state=active]:border-input dark:data-[state=active]:bg-input/30 text-foreground dark:text-muted-foreground inline-flex h-[calc(100%-1px)] flex-1 items-center justify-center gap-1.5 rounded-xl border border-transparent px-2 py-1 text-sm font-medium whitespace-nowrap transition-[color,box-shadow] focus-visible:ring-[3px] focus-visible:outline-1 disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4",
        className
      )}
      {...props}
    />
  );
}

/**
 * 📌 TabsContent
 *
 * Contenedor que muestra el contenido de la pestaña activa.
 *
 * ✅ Props (heredadas de `TabsPrimitive.Content`):
 * - value: string → debe coincidir con el `value` de un `TabsTrigger`.
 * - className: string → estilos adicionales.
 *
 * ⚠️ Si no coincide el `value` con ningún `TabsTrigger`, el contenido nunca se mostrará.
 */
function TabsContent({ className, ...props }) {
  return (
    <TabsPrimitive.Content
      data-slot="tabs-content"
      className={cn("flex-1 outline-none", className)}
      {...props}
    />
  );
}

export { Tabs, TabsList, TabsTrigger, TabsContent };
