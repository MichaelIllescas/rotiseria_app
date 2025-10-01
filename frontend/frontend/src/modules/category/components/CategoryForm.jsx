// components/CategoryForm.js
import { useState, useEffect } from 'react';
import { Button } from '../../../ui/button';
import { Input } from '../../../ui/input';
import { Label } from '../../../ui/label';
import { Card, CardContent, CardHeader, CardTitle } from '../../../ui/card';
import '../styles/categoryForm.css'


export function CategoryForm({ 
  category = null, 
  onSave, 
  onCancel, 
  isSaving = false, 
  errors = {} 
}) {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    active: true,
  });

  useEffect(() => {
    if (category) {
      setFormData({
        name: category.name || '',
        description: category.description || '',
        active: category.active !== undefined ? category.active : true,
      });
    }
  }, [category]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave(formData);
  };

  return (
    <Card className="card">
      <CardHeader className="card-header">
        <CardTitle className="card-title">
          {category ? 'Editar Categoría' : 'Nueva Categoría'}
        </CardTitle>
      </CardHeader>
      <CardContent>
        <form className="space-y-4" onSubmit={handleSubmit} noValidate>
          <div>
            <Label htmlFor="name">Nombre</Label>
            <Input
              id="name"
              name="name"
              type="text"
              value={formData.name}
              onChange={handleChange}
              placeholder="Nombre de la categoría"
              required
            />
            {errors.name && <div className="error-message">{errors.name}</div>}
          </div>

          <div>
            <Label htmlFor="description">Descripción</Label>
            <Input
              id="description"
              name="description"
              type="text"
              value={formData.description}
              onChange={handleChange}
              placeholder="Descripción de la categoría"
            />
            {errors.description && <div className="error-message">{errors.description}</div>}
          </div>

     

          {errors.form && <div className="error-message">{errors.form}</div>}

          <div style={{ display: "flex", gap: 8 }}>
            <Button
              type="button"
              variant="outline"
              size="sm"
              onClick={onCancel}
              disabled={isSaving}
              className="w-full"
            >
              Cancelar
            </Button>

            <Button
              type="submit"
              variant="default"
              size="sm"
              disabled={isSaving}
              className="w-full"
            >
              {isSaving ? "Guardando..." : "Guardar"}
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  );
}