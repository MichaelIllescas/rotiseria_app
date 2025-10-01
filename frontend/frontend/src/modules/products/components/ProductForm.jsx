import React, { useState, useEffect } from 'react';
import { Button } from '../../../ui/button';
import { Input } from '../../../ui/input';
import { Label } from '../../../ui/label';
import { Card, CardContent, CardHeader, CardTitle } from '../../../ui/card';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../../ui/select';
import '../styles/productForm.css'
// Props: categories es la lista de categorías para el dropdown
export function ProductForm({ 
  product = null, 
  categories = [], 
  onSave, 
  onCancel, 
  isSaving = false, 
  errors = {} 
}) {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: '',
    stock: '',
    categoryId: '',
    active: true,
    imageUrl: '',
  });

  useEffect(() => {
    if (product) {
      setFormData({
        name: product.name || '',
        description: product.description || '',
        price: product.price || '',
        stock: product.stock || '',
        categoryId: product.categoryId || '',
        active: product.active !== undefined ? product.active : true,
        imageUrl: product.imageUrl || '',
      });
    }
  }, [product]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };
  
  const handleSelectChange = (value) => {
    setFormData(prev => ({ ...prev, categoryId: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Convertir price y stock a números antes de enviar
    const dataToSave = {
        ...formData,
        price: parseFloat(formData.price) || 0,
        stock: parseInt(formData.stock) || 0,
    };
    onSave(dataToSave);
  };

  return (
    // envolvemos con las clases que el CSS espera (.products-list .modal-overlay ...)
    <div className="products-list">
      <div className="modal-overlay">
        <Card className="card">
          <CardHeader>
            <CardTitle>{product ? 'Editar Producto' : 'Nuevo Producto'}</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} noValidate>
              <div>
                <Label htmlFor="name">Nombre del Producto</Label>
                <Input id="name" name="name" value={formData.name} onChange={handleChange} placeholder="Ej: Pollo Asado" required />
                {errors.name && <div className="error-message">{errors.name}</div>}
              </div>

              <div>
                <Label htmlFor="description">Descripción</Label>
                <Input id="description" name="description" value={formData.description} onChange={handleChange} placeholder="Descripción detallada" />
                {errors.description && <div className="error-message">{errors.description}</div>}
              </div>

              <div className="form-grid">
                <div>
                  <Label htmlFor="price">Precio</Label>
                  <Input id="price" name="price" type="number" step="0.01" value={formData.price} onChange={handleChange} placeholder="15.99" required />
                  {errors.price && <div className="error-message">{errors.price}</div>}
                </div>
                <div>
                  <Label htmlFor="stock">Stock</Label>
                  <Input id="stock" name="stock" type="number" value={formData.stock} onChange={handleChange} placeholder="25" required />
                  {errors.stock && <div className="error-message">{errors.stock}</div>}
                </div>
              </div>
              
              <div>
                <Label htmlFor="categoryId">Categoría</Label>
                <Select value={formData.categoryId} onValueChange={handleSelectChange}>
                  <SelectTrigger data-slot="select-trigger">
                    <SelectValue placeholder="Selecciona una categoría" />
                  </SelectTrigger>
                  <SelectContent data-slot="select-content">
                    {categories.map(cat => (
                      <SelectItem key={cat.id} value={String(cat.id)} data-slot="select-item">{cat.name}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
                {errors.categoryId && <div className="error-message">{errors.categoryId}</div>}
              </div>

              <div>
                <Label htmlFor="imageUrl">URL de la Imagen (opcional)</Label>
                <Input id="imageUrl" name="imageUrl" type="url" value={formData.imageUrl} onChange={handleChange} placeholder="https://ejemplo.com/imagen.jpg" />
              </div>

          

              {errors.form && <div className="error-message">{errors.form}</div>}

              <div className="form-actions">
                <Button type="button" variant="outline" onClick={onCancel} disabled={isSaving}>Cancelar</Button>
                <Button type="submit" disabled={isSaving}>{isSaving ? "Guardando..." : "Guardar"}</Button>
              </div>
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}