import { useState, useEffect } from 'react';
import { Button } from '../../../ui/button';
import { Input } from '../../../ui/input';
import { Label } from '../../../ui/label';
import { Card, CardContent, CardHeader, CardTitle } from '../../../ui/card';
import '../styles/businessForm.css';

export function BusinessForm({ 
  business = null, 
  onSave, 
  onCancel, 
  isSaving = false, 
  errors = {} 
}) {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    email: '',
    phone: '',
    address: '',
    active: true,
  });

  useEffect(() => {
    if (business) {
      setFormData({
        name: business.name || '',
        description: business.description || '',
        email: business.email || '',
        phone: business.phone || '',
        address: business.address || '',
        active: business.active !== undefined ? business.active : true,
      });
    }
  }, [business]);

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
          {business ? 'Editar Negocio' : 'Nuevo Negocio'}
        </CardTitle>
      </CardHeader>
      <CardContent>
        <form className="space-y-4" onSubmit={handleSubmit} noValidate>
          <div className="form-grid">
            <div>
              <Label htmlFor="name">Nombre *</Label>
              <Input
                id="name"
                name="name"
                type="text"
                value={formData.name}
                onChange={handleChange}
                placeholder="Nombre del negocio"
                required
              />
              {errors.name && <div className="error-message">{errors.name}</div>}
            </div>

            <div>
              <Label htmlFor="email">Email *</Label>
              <Input
                id="email"
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="email@ejemplo.com"
                required
              />
              {errors.email && <div className="error-message">{errors.email}</div>}
            </div>
          </div>

          <div className="form-grid">
            <div>
              <Label htmlFor="phone">Teléfono *</Label>
              <Input
                id="phone"
                name="phone"
                type="tel"
                value={formData.phone}
                onChange={handleChange}
                placeholder="+54 9 11 1234-5678"
                required
              />
              {errors.phone && <div className="error-message">{errors.phone}</div>}
            </div>

            <div>
              <Label htmlFor="address">Dirección *</Label>
              <Input
                id="address"
                name="address"
                type="text"
                value={formData.address}
                onChange={handleChange}
                placeholder="Dirección completa"
                required
              />
              {errors.address && <div className="error-message">{errors.address}</div>}
            </div>
          </div>

          <div>
            <Label htmlFor="description">Descripción</Label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              placeholder="Descripción del negocio (opcional)"
              rows={3}
              className="description-textarea"
            />
            {errors.description && <div className="error-message">{errors.description}</div>}
          </div>

          <div className="checkbox-container">
            <input
              id="active"
              name="active"
              type="checkbox"
              checked={formData.active}
              onChange={handleChange}
              className="checkbox-input"
            />
            <Label htmlFor="active" className="checkbox-label">
              Negocio activo
            </Label>
          </div>

          {errors.form && <div className="error-message">{errors.form}</div>}

          <div className="button-container">
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