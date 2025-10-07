import React, { useState, useEffect } from "react";
import { Button } from "../../../ui/button";
import { Input } from "../../../ui/input";
import { Label } from "../../../ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "../../../ui/card";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../../../ui/select";
import "../styles/productForm.css";

export function ProductForm({
  product = null,
  categories = [],
  onSave,
  onCancel,
  isSaving = false,
  errors = {},
  imageUrlBaseUrl = "",
}) {
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    price: "",
    dailyStock: "",
    categoryId: "",
    active: true,
    image: null,
  });

  const [imagePreview, setImagePreview] = useState(null);

  useEffect(() => {
    if (product) {
      setFormData({
        name: product.name || "",
        description: product.description || "",
        price: product.price || "",
        dailyStock: product.dailyStock || "",
        categoryId: product.categoryId || "",
        active: product.active ?? true,
        image: null,
      });
      setImagePreview(product.imageUrl || null);
    }
  }, [product]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSelectChange = (value) => {
    setFormData((prev) => ({ ...prev, categoryId: value }));
  };

  const handleImageChange = (e) => {
    const file = e.target.files?.[0];
    if (file) {
      setFormData((prev) => ({ ...prev, image: file }));
      setImagePreview(URL.createObjectURL(file));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const dataToSave = {
      ...formData,
      price: parseFloat(formData.price) || 0,
      dailyStock: parseInt(formData.dailyStock) || 0,
      imageUrl: product?.imageUrl || null,
    };
    onSave(dataToSave);
  };

  return (
    <div className="product-form-modal">
      <Card className="product-form-card">
        <CardHeader className="product-form-card-header">
          <CardTitle className="product-form-card-title">
            {product ? "Editar Producto" : "Nuevo Producto"}
          </CardTitle>
        </CardHeader>

        <CardContent>
          <form onSubmit={handleSubmit} noValidate>
            <div>
              <Label className="product-form-label" htmlFor="name">
                Nombre del Producto
              </Label>
              <Input
                className="product-form-input"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                placeholder="Ej: Pollo Asado"
                required
              />
              {errors.name && (
                <div className="product-form-error">{errors.name}</div>
              )}
            </div>

            <div>
              <Label className="product-form-label" htmlFor="description">
                Descripción
              </Label>
              <Input
                className="product-form-input"
                id="description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                placeholder="Descripción detallada"
              />
              {errors.description && (
                <div className="product-form-error">{errors.description}</div>
              )}
            </div>

            <div className="product-form-grid">
              <div>
                <Label className="product-form-label" htmlFor="price">
                  Precio
                </Label>
                <Input
                  className="product-form-input"
                  id="price"
                  name="price"
                  type="number"
                  step="0.01"
                  value={formData.price}
                  onChange={handleChange}
                  placeholder="15.99"
                  required
                />
                {errors.price && (
                  <div className="product-form-error">{errors.price}</div>
                )}
              </div>

              <div>
                <Label className="product-form-label" htmlFor="dailyStock">
                  Stock Diario
                </Label>
                <Input
                  className="product-form-input"
                  id="dailyStock"
                  name="dailyStock"
                  type="number"
                  value={formData.dailyStock}
                  onChange={handleChange}
                  placeholder="25"
                  required
                />
                {errors.dailyStock && (
                  <div className="product-form-error">{errors.dailyStock}</div>
                )}
              </div>
            </div>

            <div>
              <Label className="product-form-label" htmlFor="categoryId">
                Categoría
              </Label>
              <Select
                value={formData.categoryId}
                onValueChange={handleSelectChange}
              >
                <SelectTrigger
                  className="product-form-select-trigger"
                  data-slot="select-trigger"
                >
                  <SelectValue placeholder="Selecciona una categoría" />
                </SelectTrigger>
                <SelectContent data-slot="select-content">
                  {categories.map((cat) => (
                    <SelectItem
                      key={cat.id}
                      value={String(cat.id)}
                      data-slot="select-item"
                    >
                      {cat.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              {errors.categoryId && (
                <div className="product-form-error">{errors.categoryId}</div>
              )}
            </div>

            <div>
              <Label className="product-form-label" htmlFor="image">
                Imagen del Producto
              </Label>
              <Input
                className="product-form-input"
                id="image"
                name="image"
                type="file"
                accept="image/*"
                onChange={handleImageChange}
              />
              {imagePreview && (
                <div className="product-form-image-preview">
                  <img
                    src={
                      imagePreview.startsWith("blob:")
                        ? imagePreview
                        : `${imageUrlBaseUrl}${imagePreview}`
                    }
                    alt="Vista previa"
                  />
                </div>
              )}
            </div>

            {errors.form && (
              <div className="product-form-error">{errors.form}</div>
            )}

            <div className="product-form-actions">
              <Button
                type="button"
                variant="outline"
                onClick={onCancel}
                disabled={isSaving}
                className="product-form-btn-cancel"
              >
                Cancelar
              </Button>

              <Button type="submit" disabled={isSaving}>
                {isSaving ? "Guardando..." : "Guardar"}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
