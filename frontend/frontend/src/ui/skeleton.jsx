import { cn } from "./utils";

/**
 * 🔹 Skeleton
 *
 * Es un componente de "esqueleto" o "carga en progreso".
 * Se utiliza para mostrar un bloque con animación de "pulse"
 * mientras se cargan datos, de modo que el usuario vea
 * que algo está cargando en lugar de un espacio vacío.
 *
 * 📌 Props:
 * - `className` (opcional): permite aplicar clases extra
 *   para personalizar el ancho, alto, márgenes, etc.
 * - `...props`: cualquier otra propiedad válida de un <div>
 *   (por ejemplo: `id`, `role`, `style`, `onClick`, etc).
 *
 * 📌 Funcionamiento:
 * - Renderiza un <div> con estilos base de "esqueleto":
 *   - `bg-accent`: color de fondo
 *   - `animate-pulse`: animación de carga
 *   - `rounded-md`: bordes redondeados
 * - Se pueden extender los estilos pasándole `className`.
 *
 * Ejemplo de uso:
 * ```jsx
 * <Skeleton className="w-32 h-6" />
 * ```
 */
function Skeleton({ className, ...props }) {
  return (
    <div
      data-slot="skeleton" // atributo de control útil para debug/testing
      className={cn(
        "bg-accent animate-pulse rounded-md", // estilos base
        className // estilos extra personalizados
      )}
      {...props} // permite pasar cualquier otra prop al <div>
    />
  );
}

export { Skeleton };
