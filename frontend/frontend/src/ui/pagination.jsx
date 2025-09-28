import * as React from "react";
import {
  ChevronLeftIcon,
  ChevronRightIcon,
  MoreHorizontalIcon,
} from "lucide-react";

import { cn } from "./utils";
import { Button, buttonVariants } from "./button";

/**
 * 📌 Pagination
 *
 * Contenedor principal del componente de paginación.
 * Renderiza un <nav> con `role="navigation"` para accesibilidad.
 *
 * ✅ Props:
 * - className: (string) → estilos personalizados.
 *
 * ⚠️ Si no se le pasan props, muestra un contenedor vacío.
 */
function Pagination({ className, ...props }) {
  return (
    <nav
      role="navigation"
      aria-label="pagination"
      data-slot="pagination"
      className={cn("mx-auto flex w-full justify-center", className)}
      {...props}
    />
  );
}

/**
 * 📌 PaginationContent
 *
 * Lista (<ul>) que agrupa los ítems de la paginación.
 *
 * ✅ Props:
 * - className: estilos adicionales.
 *
 * ⚠️ Si no se le pasan children → queda como <ul> vacío.
 */
function PaginationContent({ className, ...props }) {
  return (
    <ul
      data-slot="pagination-content"
      className={cn("flex flex-row items-center gap-1", className)}
      {...props}
    />
  );
}

/**
 * 📌 PaginationItem
 *
 * Representa un ítem de la paginación (wrapper <li>).
 * Se usa para envolver cada link o elemento (ej: página, previous, next).
 */
function PaginationItem({ ...props }) {
  return <li data-slot="pagination-item" {...props} />;
}

/**
 * 📌 PaginationLink
 *
 * Enlace individual de la paginación.
 *
 * ✅ Props:
 * - isActive: (boolean) → si es la página actual, cambia el estilo.
 * - size: (string) → tamaño del botón (default: "icon").
 * - className: estilos adicionales.
 *
 * ⚠️ Si no se pasa `isActive`, el link se ve como un botón "ghost" (inactivo).
 * ⚠️ Si no se pasa `href`, no llevará a ninguna página (solo renderiza un <a> vacío).
 */
function PaginationLink({ className, isActive, size = "icon", ...props }) {
  return (
    <a
      aria-current={isActive ? "page" : undefined}
      data-slot="pagination-link"
      data-active={isActive}
      className={cn(
        buttonVariants({
          variant: isActive ? "outline" : "ghost",
          size,
        }),
        className
      )}
      {...props}
    />
  );
}

/**
 * 📌 PaginationPrevious
 *
 * Botón/link que lleva a la página anterior.
 *
 * ✅ Props:
 * - className: estilos adicionales.
 *
 * ⚠️ Si no se le pasa `href`, no funcionará como enlace (solo se verá como botón).
 */
function PaginationPrevious({ className, ...props }) {
  return (
    <PaginationLink
      aria-label="Go to previous page"
      size="default"
      className={cn("gap-1 px-2.5 sm:pl-2.5", className)}
      {...props}
    >
      <ChevronLeftIcon />
      <span className="hidden sm:block">Previous</span>
    </PaginationLink>
  );
}

/**
 * 📌 PaginationNext
 *
 * Botón/link que lleva a la página siguiente.
 *
 * ✅ Props:
 * - className: estilos adicionales.
 *
 * ⚠️ Si no se le pasa `href`, no funcionará como enlace (solo se verá como botón).
 */
function PaginationNext({ className, ...props }) {
  return (
    <PaginationLink
      aria-label="Go to next page"
      size="default"
      className={cn("gap-1 px-2.5 sm:pr-2.5", className)}
      {...props}
    >
      <span className="hidden sm:block">Next</span>
      <ChevronRightIcon />
    </PaginationLink>
  );
}

/**
 * 📌 PaginationEllipsis
 *
 * Indicador visual (…) que muestra que hay más páginas entre medio.
 *
 * ✅ Props:
 * - className: estilos adicionales.
 *
 * ⚠️ No es interactivo, solo decorativo. 
 */
function PaginationEllipsis({ className, ...props }) {
  return (
    <span
      aria-hidden
      data-slot="pagination-ellipsis"
      className={cn("flex size-9 items-center justify-center", className)}
      {...props}
    >
      <MoreHorizontalIcon className="size-4" />
      <span className="sr-only">More pages</span>
    </span>
  );
}

/* 🔹 Exportación */
export {
  Pagination,
  PaginationContent,
  PaginationLink,
  PaginationItem,
  PaginationPrevious,
  PaginationNext,
  PaginationEllipsis,
};
