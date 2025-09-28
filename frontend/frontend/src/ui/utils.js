// Importamos dos librerías clave:
// 1. clsx → nos permite condicionar clases de forma más limpia.
// 2. tailwind-merge → se encarga de "fusionar" clases de Tailwind evitando conflictos.
//    Ejemplo: "p-2 p-4" → tailwind-merge deja solo "p-4".
import { clsx } from "clsx";
import { twMerge } from "tailwind-merge";

/**
 * 📌 cn (class names)
 *
 * Función auxiliar para construir strings de clases de manera segura y elegante.
 * Se usa en todos los componentes para no tener que concatenar manualmente.
 *
 * ✅ ¿Qué hace?
 * 1. Recibe múltiples valores de clases (string, objeto condicional, array, etc.).
 * 2. clsx → filtra y construye un string válido.
 * 3. twMerge → resuelve conflictos entre clases de Tailwind (quedando la última más fuerte).
 *
 * ⚠️ Ejemplo de uso:
 * cn("p-2", "bg-red-500", condition && "text-white")
 *
 * Resultado:
 * - Si `condition` es true → "p-2 bg-red-500 text-white"
 * - Si `condition` es false → "p-2 bg-red-500"
 */
export function cn(...inputs) {
  return twMerge(clsx(inputs));
}
