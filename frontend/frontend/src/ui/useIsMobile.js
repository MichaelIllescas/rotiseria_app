import * as React from "react";

// 📌 Punto de quiebre para mobile (definido en píxeles).
// Todo lo menor a 768px se considera "mobile".
const MOBILE_BREAKPOINT = 768;

/**
 * 📌 useIsMobile
 *
 * Hook personalizado que devuelve un booleano indicando si
 * la pantalla actual es "mobile" según un breakpoint definido.
 *
 * ✅ ¿Cómo funciona?
 * 1. Crea un estado interno `isMobile` que arranca en `undefined`.
 * 2. Usa un `useEffect` para:
 *    - Crear un `matchMedia` con la condición `(max-width: 767px)`.
 *    - Definir un listener (`onChange`) que actualiza el estado
 *      cuando el ancho de la ventana cambia.
 *    - Setear el valor inicial en el primer render.
 *    - Limpiar el listener al desmontar el componente.
 *
 * ✅ Retorna:
 * - `true` → si la pantalla actual es menor al breakpoint (mobile).
 * - `false` → si la pantalla es mayor o igual al breakpoint (desktop/tablet).
 *
 * ⚠️ Importante:
 * - Al principio devuelve `false` hasta que se ejecute el `useEffect`
 *   y determine el tamaño real (evita errores en SSR).
 */
export function useIsMobile() {
  // Estado que guarda si estamos en mobile (true/false).
  // Se inicializa en `undefined` hasta que el efecto determine el valor real.
  const [isMobile, setIsMobile] = React.useState(undefined);

  React.useEffect(() => {
    // 🎯 Creamos un "media query listener" para (max-width: 767px)
    const mql = window.matchMedia(`(max-width: ${MOBILE_BREAKPOINT - 1}px)`);

    // Función que se dispara cada vez que cambia el tamaño de la pantalla
    const onChange = () => {
      setIsMobile(window.innerWidth < MOBILE_BREAKPOINT);
    };

    // 📌 Asignamos el listener
    mql.addEventListener("change", onChange);

    // 📌 Seteamos el valor inicial (ej: en el primer render)
    setIsMobile(window.innerWidth < MOBILE_BREAKPOINT);

    // 🧹 Cleanup → remover listener al desmontar
    return () => mql.removeEventListener("change", onChange);
  }, []);

  // Devuelve true o false (si estaba undefined lo fuerza a booleano con !!)
  return !!isMobile;
}
