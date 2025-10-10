/**
 * Agrupa productos dentro de sus categorías correspondientes
 * según el campo categoryId.
 *
 * @param {Array} categories - lista de categorías [{id, name}]
 * @param {Array} products - lista de productos [{id, categoryId, ...}]
 * @returns {Array} lista de categorías con sus productos [{id, name, products: []}]
 */
export const groupProductsByCategory = (categories, products) => {
  // Validar entradas
  if (!Array.isArray(categories)) {
    console.warn('groupProductsByCategory: categories debe ser un array');
    return [];
  }
  
  if (!Array.isArray(products)) {
    console.warn('groupProductsByCategory: products debe ser un array');
    products = [];
  }

  return categories.map((cat) => ({
    ...cat,
    products: products.filter((p) => p.categoryId === cat.id),
  }));
};
