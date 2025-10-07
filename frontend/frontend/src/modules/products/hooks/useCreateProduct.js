import { useState } from "react";
import { uploadImage, create } from "../services/productService";
import { toast } from '../../../ui/toaster';


/**
 * Hook para manejar la creación de productos.
 * Primero sube la imagen, luego registra el producto.
 */
export const useCreateProduct = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const createProduct = async (productData) => {
    setLoading(true);
    setError(null);

    try {
      let imageUrl = null;

      // 1️⃣ Subir la imagen si se proporcionó
      if (productData.image instanceof File) {
        imageUrl = await uploadImage(productData.image);
      }

      // 2️⃣ Crear el producto con la URL obtenida
      const payload = {
        categoryId: productData.categoryId,
        name: productData.name,
        description: productData.description,
        price: productData.price,
        dailyStock: productData.dailyStock,
        imageUrl, // podría ser null si no se subió imagen
      };

      const response = await create(payload);
      toast.success("Producto registrado con éxito");
      return response; // devolvemos el producto creado al componente

    } catch (err) {
      console.error("Error creando producto:", err);
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { createProduct, loading, error };
};
