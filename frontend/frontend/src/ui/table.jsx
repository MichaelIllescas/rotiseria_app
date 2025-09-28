"use client";

import * as React from "react";
import { cn } from "./utils";

/**
 * 📌 Table
 *
 * Componente contenedor de tablas con estilos consistentes.
 * Envuelve el <table> en un <div> con overflow-x para soportar scroll horizontal.
 *
 * ✅ Props:
 * - className: string → clases adicionales.
 * - ...props: cualquier otra prop válida de <table>.
 *
 * ⚠️ Si no pasás `children`, la tabla quedará vacía.
 */
function Table({ className, ...props }) {
  return (
    <div
      data-slot="table-container"
      className="relative w-full overflow-x-auto"
    >
      <table
        data-slot="table"
        className={cn("w-full caption-bottom text-sm", className)}
        {...props}
      />
    </div>
  );
}

/**
 * 📌 TableHeader
 *
 * Encabezado de la tabla (<thead>).
 * Aplica borde inferior a cada fila interna.
 */
function TableHeader({ className, ...props }) {
  return (
    <thead
      data-slot="table-header"
      className={cn("[&_tr]:border-b", className)}
      {...props}
    />
  );
}

/**
 * 📌 TableBody
 *
 * Cuerpo de la tabla (<tbody>).
 * Elimina el borde inferior de la última fila.
 */
function TableBody({ className, ...props }) {
  return (
    <tbody
      data-slot="table-body"
      className={cn("[&_tr:last-child]:border-0", className)}
      {...props}
    />
  );
}

/**
 * 📌 TableFooter
 *
 * Pie de tabla (<tfoot>).
 * Fondo gris, borde superior y texto en negrita.
 */
function TableFooter({ className, ...props }) {
  return (
    <tfoot
      data-slot="table-footer"
      className={cn(
        "bg-muted/50 border-t font-medium [&>tr]:last:border-b-0",
        className
      )}
      {...props}
    />
  );
}

/**
 * 📌 TableRow
 *
 * Fila de tabla (<tr>).
 * - Aplica borde inferior y hover con fondo gris.
 * - Admite estado `data-state="selected"` para marcar filas seleccionadas.
 */
function TableRow({ className, ...props }) {
  return (
    <tr
      data-slot="table-row"
      className={cn(
        "hover:bg-muted/50 data-[state=selected]:bg-muted border-b transition-colors",
        className
      )}
      {...props}
    />
  );
}

/**
 * 📌 TableHead
 *
 * Celda de encabezado (<th>).
 * - Fuente en negrita, alineación izquierda.
 * - Soporta checkboxes alineados automáticamente.
 */
function TableHead({ className, ...props }) {
  return (
    <th
      data-slot="table-head"
      className={cn(
        "text-foreground h-10 px-2 text-left align-middle font-medium whitespace-nowrap",
        "[&:has([role=checkbox])]:pr-0 [&>[role=checkbox]]:translate-y-[2px]",
        className
      )}
      {...props}
    />
  );
}

/**
 * 📌 TableCell
 *
 * Celda de datos (<td>).
 * - Padding base, alineación vertical centrada.
 * - Soporta checkboxes alineados.
 */
function TableCell({ className, ...props }) {
  return (
    <td
      data-slot="table-cell"
      className={cn(
        "p-2 align-middle whitespace-nowrap",
        "[&:has([role=checkbox])]:pr-0 [&>[role=checkbox]]:translate-y-[2px]",
        className
      )}
      {...props}
    />
  );
}

/**
 * 📌 TableCaption
 *
 * Leyenda de la tabla (<caption>).
 * Texto gris y pequeño debajo de la tabla.
 */
function TableCaption({ className, ...props }) {
  return (
    <caption
      data-slot="table-caption"
      className={cn("text-muted-foreground mt-4 text-sm", className)}
      {...props}
    />
  );
}

export {
  Table,
  TableHeader,
  TableBody,
  TableFooter,
  TableHead,
  TableRow,
  TableCell,
  TableCaption,
};
