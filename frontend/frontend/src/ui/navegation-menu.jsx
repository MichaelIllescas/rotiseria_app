import * as React from "react";
import * as NavigationMenuPrimitive from "@radix-ui/react-navigation-menu";
import { cva } from "class-variance-authority";
import { ChevronDownIcon } from "lucide-react";

import { cn } from "./utils";

/**
 * 📌 NavigationMenu
 *
 * Barra de navegación con menús desplegables, similar a los menús de
 * navegación de sitios web complejos.
 *
 * ✅ Props:
 * - className: (string) → estilos personalizados.
 * - children: (ReactNode) → los menús internos.
 * - viewport: (boolean) → muestra o no el viewport para los contenidos (por defecto true).
 *
 * ⚠️ Si no pasás `children` → se renderiza la barra vacía.
 */
function NavigationMenu({ className, children, viewport = true, ...props }) {
  return (
    <NavigationMenuPrimitive.Root
      data-slot="navigation-menu"
      data-viewport={viewport}
      className={cn(
        "group/navigation-menu relative flex max-w-max flex-1 items-center justify-center",
        className
      )}
      {...props}
    >
      {children}
      {viewport && <NavigationMenuViewport />}
    </NavigationMenuPrimitive.Root>
  );
}

/**
 * 📌 NavigationMenuList
 *
 * Contenedor principal de los items de navegación (lista de menús).
 */
function NavigationMenuList({ className, ...props }) {
  return (
    <NavigationMenuPrimitive.List
      data-slot="navigation-menu-list"
      className={cn(
        "group flex flex-1 list-none items-center justify-center gap-1",
        className
      )}
      {...props}
    />
  );
}

/**
 * 📌 NavigationMenuItem
 *
 * Representa un ítem de la barra de navegación.
 * - Generalmente se combina con `NavigationMenuTrigger` y `NavigationMenuContent`.
 */
function NavigationMenuItem({ className, ...props }) {
  return (
    <NavigationMenuPrimitive.Item
      data-slot="navigation-menu-item"
      className={cn("relative", className)}
      {...props}
    />
  );
}

/**
 * 📌 navigationMenuTriggerStyle
 *
 * Estilos reutilizables para los triggers de menú.
 */
const navigationMenuTriggerStyle = cva(
  "group inline-flex h-9 w-max items-center justify-center rounded-md bg-background px-4 py-2 text-sm font-medium hover:bg-accent hover:text-accent-foreground focus:bg-accent focus:text-accent-foreground disabled:pointer-events-none disabled:opacity-50 data-[state=open]:bg-accent/50 focus-visible:ring-ring/50 transition-[color,box-shadow] outline-none focus-visible:ring-[3px]"
);

/**
 * 📌 NavigationMenuTrigger
 *
 * Botón que abre un menú desplegable.
 *
 * ✅ Props:
 * - children: (string | ReactNode) → el texto o contenido del trigger (obligatorio).
 *
 * ⚠️ Si no se pasa `children`, no habrá texto para abrir el menú.
 */
function NavigationMenuTrigger({ className, children, ...props }) {
  return (
    <NavigationMenuPrimitive.Trigger
      data-slot="navigation-menu-trigger"
      className={cn(navigationMenuTriggerStyle(), "group", className)}
      {...props}
    >
      {children}
      <ChevronDownIcon
        className="relative top-[1px] ml-1 size-3 transition duration-300 group-data-[state=open]:rotate-180"
        aria-hidden="true"
      />
    </NavigationMenuPrimitive.Trigger>
  );
}

/**
 * 📌 NavigationMenuContent
 *
 * Contenedor del contenido desplegable de un ítem.
 *
 * ✅ Props:
 * - className: estilos personalizados.
 * - children: contenido interno (links, grids, etc.).
 */
function NavigationMenuContent({ className, ...props }) {
  return (
    <NavigationMenuPrimitive.Content
      data-slot="navigation-menu-content"
      className={cn(
        "data-[motion^=from-]:animate-in data-[motion^=to-]:animate-out data-[motion^=from-]:fade-in data-[motion^=to-]:fade-out top-0 left-0 w-full p-2 md:absolute md:w-auto",
        "group-data-[viewport=false]/navigation-menu:bg-popover group-data-[viewport=false]/navigation-menu:rounded-md group-data-[viewport=false]/navigation-menu:shadow",
        className
      )}
      {...props}
    />
  );
}

/**
 * 📌 NavigationMenuViewport
 *
 * Vista desplegable grande para mostrar el contenido de los menús.
 * Suele usarse para menús complejos tipo "mega menu".
 */
function NavigationMenuViewport({ className, ...props }) {
  return (
    <div className="absolute top-full left-0 isolate z-50 flex justify-center">
      <NavigationMenuPrimitive.Viewport
        data-slot="navigation-menu-viewport"
        className={cn(
          "origin-top-center bg-popover text-popover-foreground data-[state=open]:animate-in data-[state=closed]:animate-out relative mt-1.5 h-[var(--radix-navigation-menu-viewport-height)] w-full overflow-hidden rounded-md border shadow md:w-[var(--radix-navigation-menu-viewport-width)]",
          className
        )}
        {...props}
      />
    </div>
  );
}

/**
 * 📌 NavigationMenuLink
 *
 * Link dentro del menú (redirige a otra página o sección).
 *
 * ✅ Props:
 * - href: (string) → URL a la que redirige (obligatorio).
 *
 * ⚠️ Si no se pasa `href`, no funcionará como enlace.
 */
function NavigationMenuLink({ className, ...props }) {
  return (
    <NavigationMenuPrimitive.Link
      data-slot="navigation-menu-link"
      className={cn(
        "hover:bg-accent hover:text-accent-foreground focus:bg-accent focus:text-accent-foreground flex flex-col gap-1 rounded-sm p-2 text-sm transition-all outline-none focus-visible:ring-[3px]",
        className
      )}
      {...props}
    />
  );
}

/**
 * 📌 NavigationMenuIndicator
 *
 * Indicador visual (pequeño triángulo) que señala el ítem activo.
 */
function NavigationMenuIndicator({ className, ...props }) {
  return (
    <NavigationMenuPrimitive.Indicator
      data-slot="navigation-menu-indicator"
      className={cn(
        "data-[state=visible]:animate-in data-[state=hidden]:animate-out top-full z-[1] flex h-1.5 items-end justify-center overflow-hidden",
        className
      )}
      {...props}
    >
      <div className="bg-border relative top-[60%] h-2 w-2 rotate-45 rounded-tl-sm shadow-md" />
    </NavigationMenuPrimitive.Indicator>
  );
}

/* 🔹 Exportación */
export {
  NavigationMenu,
  NavigationMenuList,
  NavigationMenuItem,
  NavigationMenuContent,
  NavigationMenuTrigger,
  NavigationMenuLink,
  NavigationMenuIndicator,
  NavigationMenuViewport,
  navigationMenuTriggerStyle,
};
