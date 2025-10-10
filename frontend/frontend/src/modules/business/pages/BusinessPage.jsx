import React, { useState } from 'react';
import { Plus, Building } from 'lucide-react';
import { Button } from '../../../ui/button';
import { toast } from '../../../ui/toaster';
import { BusinessDisplay } from '../components/BusinessDisplay';
import { BusinessForm } from '../components/BusinessForm';
import { useBusinesses } from '../hooks/useBusinesses';
import { useCreateBusiness } from '../hooks/useCreateBusiness';
import { useUpdateBusiness } from '../hooks/useUpdateBusiness';
import '../styles/businessPage.css';

export const BusinessPage = () => {
  // Estados para manejar UI
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [formErrors, setFormErrors] = useState({});

  // Hooks para manejar datos
  const { businesses, loading, error, refetchBusinesses } = useBusinesses();
  const { createBusiness, loading: creatingBusiness } = useCreateBusiness();
  const { updateBusiness } = useUpdateBusiness();

  // Asumimos que solo hay una empresa por aplicación
  const currentBusiness = businesses && businesses.length > 0 ? businesses[0] : null;

  /**
   * Maneja la creación de una nueva empresa
   */
  const handleCreateBusiness = async (businessData) => {
    try {
      setFormErrors({});

      // Validaciones básicas
      const errors = {};
      if (!businessData.name?.trim()) errors.name = "Nombre requerido";
      if (!businessData.email?.trim()) errors.email = "Email requerido";
      if (!businessData.phone?.trim()) errors.phone = "Teléfono requerido";
      if (!businessData.address?.trim()) errors.address = "Dirección requerida";
      
      // Validación de email
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (businessData.email && !emailRegex.test(businessData.email)) {
        errors.email = "Email no válido";
      }

      if (Object.keys(errors).length > 0) {
        setFormErrors(errors);
        return;
      }

      await createBusiness(businessData);
      toast.success("Empresa registrada correctamente");
      refetchBusinesses();
      setShowCreateForm(false);
    } catch (err) {
      const errorMessage = err.message || "Error al registrar la empresa";
      setFormErrors({ form: errorMessage });
      toast.error(errorMessage);
    }
  };

  /**
   * Maneja la actualización de la empresa existente
   */
  const handleUpdateBusiness = async (businessData) => {
    try {
      if (!currentBusiness?.id) {
        throw new Error("No se encontró la empresa a actualizar");
      }

      await updateBusiness(currentBusiness.id, businessData);
      toast.success("Empresa actualizada correctamente");
      refetchBusinesses();
    } catch (err) {
      const errorMessage = err.message || "Error al actualizar la empresa";
      toast.error(errorMessage);
      throw err; // Re-lanzamos para que BusinessDisplay maneje el error
    }
  };

  /**
   * Cancela el formulario de creación
   */
  const handleCancelCreate = () => {
    setShowCreateForm(false);
    setFormErrors({});
  };

  /**
   * Abre el formulario de creación
   */
  const handleShowCreateForm = () => {
    setShowCreateForm(true);
    setFormErrors({});
  };

  return (
    <div className="business-page">
      <div className="business-page-container">
        <div className="page-header">
          <div className="header-content">
            <div className="title-section">
            <h1 className="page-title">
              <Building className="title-icon" />
              Gestión de Empresa
            </h1>
            <p className="page-description">
              Administra la información de tu empresa y negocio
            </p>
            
            {!currentBusiness && !loading && !showCreateForm && (
              <div className="header-actions">
                <Button
                  onClick={handleShowCreateForm}
                  className="create-button"
                  size="lg"
                >
                  <Plus className="button-icon" />
                  <span className='boton-register-text'>Registrar Empresa</span>
                </Button>
              </div>
            )}
          </div>
        </div>
      </div>

      <div className="page-content">
        {showCreateForm ? (
          <div className="form-section">
            <div className="form-header">
              <h2 className="form-title">Registrar Nueva Empresa</h2>
              <p className="form-description">
                Completa la información básica de tu empresa
              </p>
            </div>
            <BusinessForm
              onSave={handleCreateBusiness}
              onCancel={handleCancelCreate}
              isSaving={creatingBusiness}
              errors={formErrors}
            />
          </div>
        ) : (
          <div className="display-section">
            <BusinessDisplay
              business={currentBusiness}
              onUpdate={handleUpdateBusiness}
              loading={loading}
              error={error}
            />
          </div>
        )}
      </div>

      {/* Información adicional si hay empresa registrada */}
      {currentBusiness && !showCreateForm && (
        <div className="info-section">
          <div className="info-cards">
            <div className="info-card">
              <h3 className="info-card-title">Estado del Negocio</h3>
              <p className="info-card-description">
                {currentBusiness.active 
                  ? "Tu negocio está activo y operativo" 
                  : "Tu negocio está marcado como inactivo"
                }
              </p>
            </div>
            
            <div className="info-card">
              <h3 className="info-card-title">Próximos Pasos</h3>
              <p className="info-card-description">
                Con tu empresa registrada, puedes continuar configurando productos, 
                categorías y comenzar a gestionar pedidos.
              </p>
            </div>
          </div>
        </div>
      )}
      </div> {/* Cierre del business-page-container */}
    </div>
  );
};