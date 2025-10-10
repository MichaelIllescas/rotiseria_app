import { useState, useEffect } from 'react';
import { Button } from '../../../ui/button';
import { Input } from '../../../ui/input';
import { Label } from '../../../ui/label';
import { Textarea } from '../../../ui/textarea';
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
  });

  useEffect(() => {
    if (business) {
      setFormData({
        name: business.name || '',
        description: business.description || '',
        email: business.email || '',
        phone: business.phone || '',
        address: business.address || '',
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
    onSave({ ...formData, active: true });
  };

  const handleOverlayClick = (e) => {
    if (e.target === e.currentTarget && onCancel) {
      onCancel();
    }
  };

  return (
    <div className="business-form-modal" onClick={handleOverlayClick}>
      <div className="business-form">
        <Card className="card">
          <CardHeader className="card-header">
            <CardTitle className="card-title">
              {business ? 'Editar Negocio' : 'Nuevo Negocio'}
            </CardTitle>
          </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} noValidate>
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
              <Textarea
                id="description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                placeholder="Descripción del negocio (opcional)"
                rows={4}
              />
              {errors.description && <div className="error-message">{errors.description}</div>}
            </div>

            {errors.form && <div className="error-message">{errors.form}</div>}

            <div className="button-container">
              <Button
                type="button"
                variant="ghost"
                size="sm"
                onClick={onCancel}
                disabled={isSaving}
                className="w-full btn-cancel"
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
      </div>
    </div>
  );
}