/**
 * Utilidades para formateo de fechas
 * Funciones reutilizables para mostrar fechas en formato argentino
 */

/**
 * Formatea una fecha en formato legible para argentinos
 * @param {string|Date} date - Fecha a formatear (ISO string o objeto Date)
 * @param {Object} options - Opciones de formateo
 * @param {boolean} options.includeTime - Si incluir la hora (default: true)
 * @param {boolean} options.includeSeconds - Si incluir segundos (default: false)
 * @param {string} options.timeZone - Zona horaria (default: 'America/Argentina/Buenos_Aires')
 * @returns {string} Fecha formateada
 * 
 * @example
 * formatDateForArgentina('2024-10-25T14:30:00Z') 
 * // Returns: "25 de octubre de 2024 a las 11:30"
 * 
 * formatDateForArgentina('2024-10-25T14:30:00Z', { includeTime: false })
 * // Returns: "25 de octubre de 2024"
 * 
 * formatDateForArgentina('2024-10-25T14:30:45Z', { includeSeconds: true })
 * // Returns: "25 de octubre de 2024 a las 11:30:45"
 */
export const formatDateForArgentina = (date, options = {}) => {
  const {
    includeTime = true,
    includeSeconds = false,
    timeZone = 'America/Argentina/Buenos_Aires'
  } = options;

  if (!date) return '';

  try {
    const dateObj = typeof date === 'string' ? new Date(date) : date;
    
    if (isNaN(dateObj.getTime())) {
      console.warn('Fecha inválida proporcionada:', date);
      return 'Fecha inválida';
    }

    // Configuración para formato argentino
    const dateOptions = {
      timeZone,
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    };

    const timeOptions = {
      timeZone,
      hour: '2-digit',
      minute: '2-digit',
      ...(includeSeconds && { second: '2-digit' }),
      hour12: false // Formato 24 horas
    };

    // Formatear fecha
    const formattedDate = dateObj.toLocaleDateString('es-AR', dateOptions);
    
    if (!includeTime) {
      return formattedDate;
    }

    // Formatear hora
    const formattedTime = dateObj.toLocaleTimeString('es-AR', timeOptions);
    
    return `${formattedDate} a las ${formattedTime}`;
    
  } catch (error) {
    console.error('Error al formatear fecha:', error);
    return 'Error en fecha';
  }
};

/**
 * Formatea una fecha en formato corto argentino
 * @param {string|Date} date - Fecha a formatear
 * @returns {string} Fecha en formato DD/MM/YYYY
 * 
 * @example
 * formatDateShort('2024-10-25T14:30:00Z') 
 * // Returns: "25/10/2024"
 */
export const formatDateShort = (date) => {
  if (!date) return '';

  try {
    const dateObj = typeof date === 'string' ? new Date(date) : date;
    
    if (isNaN(dateObj.getTime())) {
      return 'Fecha inválida';
    }

    return dateObj.toLocaleDateString('es-AR', {
      timeZone: 'America/Argentina/Buenos_Aires',
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
    
  } catch (error) {
    console.error('Error al formatear fecha corta:', error);
    return 'Error en fecha';
  }
};

/**
 * Formatea solo la hora en formato argentino
 * @param {string|Date} date - Fecha a formatear
 * @param {boolean} includeSeconds - Si incluir segundos (default: false)
 * @returns {string} Hora en formato HH:MM o HH:MM:SS
 * 
 * @example
 * formatTimeOnly('2024-10-25T14:30:00Z') 
 * // Returns: "11:30"
 */
export const formatTimeOnly = (date, includeSeconds = false) => {
  if (!date) return '';

  try {
    const dateObj = typeof date === 'string' ? new Date(date) : date;
    
    if (isNaN(dateObj.getTime())) {
      return 'Hora inválida';
    }

    return dateObj.toLocaleTimeString('es-AR', {
      timeZone: 'America/Argentina/Buenos_Aires',
      hour: '2-digit',
      minute: '2-digit',
      ...(includeSeconds && { second: '2-digit' }),
      hour12: false
    });
    
  } catch (error) {
    console.error('Error al formatear hora:', error);
    return 'Error en hora';
  }
};

/**
 * Calcula tiempo relativo en español argentino
 * @param {string|Date} date - Fecha a comparar
 * @returns {string} Tiempo relativo (ej: "hace 2 horas", "en 30 minutos")
 * 
 * @example
 * getRelativeTime('2024-10-25T12:00:00Z') 
 * // Returns: "hace 2 horas" (si ahora son las 14:00)
 */
export const getRelativeTime = (date) => {
  if (!date) return '';

  try {
    const dateObj = typeof date === 'string' ? new Date(date) : date;
    const now = new Date();
    const diffMs = now.getTime() - dateObj.getTime();
    const diffMinutes = Math.floor(diffMs / (1000 * 60));
    const diffHours = Math.floor(diffMinutes / 60);
    const diffDays = Math.floor(diffHours / 24);

    if (Math.abs(diffMinutes) < 1) {
      return 'ahora mismo';
    } else if (Math.abs(diffMinutes) < 60) {
      return diffMinutes > 0 ? `hace ${diffMinutes} minuto${diffMinutes !== 1 ? 's' : ''}` 
                             : `en ${Math.abs(diffMinutes)} minuto${Math.abs(diffMinutes) !== 1 ? 's' : ''}`;
    } else if (Math.abs(diffHours) < 24) {
      return diffHours > 0 ? `hace ${diffHours} hora${diffHours !== 1 ? 's' : ''}` 
                           : `en ${Math.abs(diffHours)} hora${Math.abs(diffHours) !== 1 ? 's' : ''}`;
    } else {
      return diffDays > 0 ? `hace ${diffDays} día${diffDays !== 1 ? 's' : ''}` 
                          : `en ${Math.abs(diffDays)} día${Math.abs(diffDays) !== 1 ? 's' : ''}`;
    }
    
  } catch (error) {
    console.error('Error al calcular tiempo relativo:', error);
    return 'Error en fecha';
  }
};