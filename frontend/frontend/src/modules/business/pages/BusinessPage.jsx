import React, { useEffect, useState } from 'react';
import { Plus, Building, Edit } from 'lucide-react';
import { Button } from '../../../ui/button';
import { toast } from '../../../ui/toaster';
import { BusinessDisplay } from '../components/BusinessDisplay';
import { BusinessForm } from '../components/BusinessForm';
import { useBusinesses } from '../hooks/useBusinesses';
import { useCreateBusiness } from '../hooks/useCreateBusiness';
import { useUpdateBusiness } from '../hooks/useUpdateBusiness';
import '../styles/businessPage.css';
import { useBusinessHours } from '../hooks/useBusinessHours';
import { BusinessHoursViewer } from '../components/BusinessHoursViewer';
import { BusinessHoursForm } from '../components/BusinessHoursForm';

export const BusinessPage = () => {
  // Estados para manejar UI
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [formErrors, setFormErrors] = useState({});
  const [editingHours, setEditingHours] = useState(false);

  // Hooks para manejar datos
  const { businesses, loading, error, refetchBusinesses } = useBusinesses();
  const { createBusiness, loading: creatingBusiness } = useCreateBusiness();
  const { updateBusiness } = useUpdateBusiness();
  const { hours, fetchHours, saveHours, loading: loadingHours } = useBusinessHours();

  useEffect(() => {
    fetchHours();
  }, []);

  if (loading) return <p>Cargando horarios...</p>;

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
   * Maneja la actualización de horarios
   */
  const handleUpdateHours = async (hoursData) => {
    try {
      await saveHours(hoursData);
      toast.success("Horarios actualizados correctamente");
      setEditingHours(false);
      fetchHours(); // Refrescar los datos
    } catch (err) {
      const errorMessage = err.message || "Error al actualizar los horarios";
      toast.error(errorMessage);
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
   * Cancela la edición de horarios
   */
  const handleCancelEditHours = () => {
    setEditingHours(false);
  };

  /**
   * Abre el formulario de creación
   */
  const handleShowCreateForm = () => {
    setShowCreateForm(true);
    setFormErrors({});
  };

  /**
   * Activa el modo de edición de horarios
   */
  const handleEditHours = () => {
    setEditingHours(true);
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

      {/* Sección de Horarios de Atención */}
      <div className="business-hours-page card p-4 mt-3">
        <div className="business-hours-header">
          <h3>Horarios de Atención</h3>
          {hours.length > 0 && !editingHours && (
            <Button
              onClick={handleEditHours}
              className="edit-hours-button"
              variant="outline"
              size="sm"
            >
              <Edit size={16} />
              Editar Horarios
            </Button>
          )}
        </div>

        {loadingHours ? (
          <p>Cargando horarios...</p>
        ) : editingHours ? (
          <BusinessHoursForm 
            onSubmit={handleUpdateHours}
            onCancel={handleCancelEditHours}
            initialData={hours}
          />
        ) : hours.length > 0 ? (
          <BusinessHoursViewer hours={hours} />
        ) : (
          <BusinessHoursForm onSubmit={saveHours} />
        )}
      </div>
      </div> {/* Cierre del business-page-container */}
    </div>
  );
};