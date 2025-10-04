import React, { useState } from 'react';
import { 
  Edit, 
  Mail, 
  Phone, 
  MapPin, 
  Building, 
  FileText,
  CheckCircle,
  XCircle,
  Loader2
} from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '../../../ui/card';
import { Button } from '../../../ui/button';
import { BusinessForm } from './BusinessForm';
import '../styles/businessDisplay.css';

/**
 * Componente para mostrar los datos de la empresa registrada
 * @param {Object} business - Datos de la empresa
 * @param {Function} onUpdate - Función llamada cuando se actualiza la empresa
 * @param {Boolean} loading - Estado de carga
 * @param {String} error - Mensaje de error si existe
 */
export function BusinessDisplay({ 
  business, 
  onUpdate, 
  loading = false, 
  error = null 
}) {
  const [isEditing, setIsEditing] = useState(false);
  const [isUpdating, setIsUpdating] = useState(false);

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleCancelEdit = () => {
    setIsEditing(false);
  };

  const handleSave = async (updatedData) => {
    try {
      setIsUpdating(true);
      await onUpdate(updatedData);
      setIsEditing(false);
    } catch (err) {
      console.error('Error updating business:', err);
    } finally {
      setIsUpdating(false);
    }
  };

  // Si está cargando
  if (loading) {
    return (
      <Card className="business-display-card">
        <CardContent className="loading-container">
          <Loader2 className="loading-spinner" />
          <p>Cargando información de la empresa...</p>
        </CardContent>
      </Card>
    );
  }

  // Si hay error
  if (error) {
    return (
      <Card className="business-display-card error-card">
        <CardContent className="error-container">
          <XCircle className="error-icon" />
          <p className="error-message">Error al cargar la información: {error}</p>
        </CardContent>
      </Card>
    );
  }

  // Si no hay empresa registrada
  if (!business) {
    return (
      <Card className="business-display-card empty-card">
        <CardContent className="empty-container">
          <Building className="empty-icon" />
          <h3>No se han registrado los datos de la empresa.</h3>
          <p>Registra los datos de tu empresa para comenzar.</p>
        </CardContent>
      </Card>
    );
  }

  // Si está en modo edición
  if (isEditing) {
    return (
      <div className="business-editing-overlay">
        <BusinessForm
          business={business}
          onSave={handleSave}
          onCancel={handleCancelEdit}
          isSaving={isUpdating}
        />
      </div>
    );
  }

  // Mostrar datos de la empresa
  return (
    <Card className="business-display-card">
      <CardHeader className="business-header">
        <div className="header-content">
          <CardTitle className="business-title">
            <Building className="title-icon" />
            {business.name}
          </CardTitle>
          <div className="header-actions">
            <div className={`status-badge ${business.active ? 'active' : 'inactive'}`}>
              {business.active ? (
                <>
                  <CheckCircle className="status-icon" />
                  Activo
                </>
              ) : (
                <>
                  <XCircle className="status-icon" />
                  Inactivo
                </>
              )}
            </div>
            <Button
              variant="outline"
              size="sm"
              onClick={handleEdit}
              className="edit-button"
            >
              <Edit className="button-icon" />
              Editar
            </Button>
          </div>
        </div>
      </CardHeader>

      <CardContent className="business-content">
        <div className="info-grid">
          <div className="info-item">
            <div className="info-label">
              <Mail className="info-icon" />
              Email
            </div>
            <div className="info-value">{business.email}</div>
          </div>

          <div className="info-item">
            <div className="info-label">
              <Phone className="info-icon" />
              Teléfono
            </div>
            <div className="info-value">{business.phone}</div>
          </div>

          <div className="info-item full-width">
            <div className="info-label">
              <MapPin className="info-icon" />
              Dirección
            </div>
            <div className="info-value">{business.address}</div>
          </div>

          {business.description && (
            <div className="info-item full-width">
              <div className="info-label">
                <FileText className="info-icon" />
                Descripción
              </div>
              <div className="info-value description">{business.description}</div>
            </div>
          )}
        </div>

        {business.createdAt && (
          <div className="business-metadata">
            <p className="metadata-text">
              Registrado el: {new Date(business.createdAt).toLocaleDateString('es-AR', {
                year: 'numeric',
                month: 'long',
                day: 'numeric'
              })}
            </p>
          </div>
        )}
      </CardContent>
    </Card>
  );
}