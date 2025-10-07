// hooks/useUpdateProduct.js
import { useState } from "react";
import { update , uploadImage} from "../services/productService";
import { toast } from '../../../ui/toaster';

export const useUpdateProduct = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const updateProduct = async (productId, productData) => {
    setLoading(true);
    setError(null);

    try {
      let imageUrl = productData.imageUrl || null;

      // Si hay una imagen nueva (File)
      if (productData.image instanceof File) {
        imageUrl = await uploadImage(productData.image);
      }

      // Crear objeto limpio para enviar al backend
      const updatedProduct = {
        ...productData,
        imageUrl,
      };

      await update(productId, updatedProduct);
      toast.success("Producto actualizado con éxito");
      return updatedProduct;
    } catch (err) {
      setError(err.message || "Error al actualizar el producto");
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    loading,
    error,
    updateProduct,
  };
};
