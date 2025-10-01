import React, { useState } from "react";
import {
  Edit,
  Trash2,
  Plus,
  Package,
  RefreshCw,
  CheckCircle,
} from "lucide-react";
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableCell,
} from "../../../ui/table";
import "../styles/categoriesList.css";
import { Button } from "../../../ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "../../../ui/card";
import { CategoryForm } from "./CategoryForm";
import { toast } from "../../../ui/toaster";

// Importaciones nombradas de los hooks personalizados
import { useCategories } from "../hooks/useCategories";
import { useCreateCategory } from "../hooks/useCreateCategory";
import { useUpdateCategory } from "../hooks/useUpdateCategory";
import { useDeleteCategory } from "../hooks/useDeleteCategory";
import { useToggleCategoryStatus } from "../hooks/useToggleCategoryStatus";
const ITEMS_PER_PAGE = 5;

/**
 * Componente principal que gestiona la lista de categorías.
 * - Muestra estadísticas, la tabla de categorías con paginación.
 * - Maneja la creación, edición, eliminación y activación/desactivación de categorías.
 * - Utiliza hooks personalizados para la lógica de negocio y la comunicación con la API.
 */
export function CategoriesList() {
  // --- Hooks para obtener datos y manejar estado de carga/errores ---
  const { categories, loading, error, refetchCategories } = useCategories();
  const { createCategory, loading: creatingCategory } = useCreateCategory();
  const { updateCategory, loading: updatingCategory } = useUpdateCategory();
  const { deleteCategory, loading: deletingCategory } = useDeleteCategory();
  const { toggleCategoryStatus, loading: togglingStatus } =
    useToggleCategoryStatus();

  // --- Estado local del componente para UI ---
  const [currentPage, setCurrentPage] = useState(1);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingCategory, setEditingCategory] = useState(null);
  const [formErrors, setFormErrors] = useState({});
  const [isSaving, setIsSaving] = useState(false);
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
  const [categoryToDelete, setCategoryToDelete] = useState(null);

  // --- Lógica de paginación ---
  const totalPages = Math.ceil(categories.length / ITEMS_PER_PAGE);
  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
  const endIndex = startIndex + ITEMS_PER_PAGE;
  const currentCategories = categories.slice(startIndex, endIndex);

  // --- Cálculo de estadísticas ---
  const activeCategories = categories.filter((c) => c.active).length;
  const totalProducts = categories.reduce(
    (sum, cat) => sum + (cat.productCount || 0),
    0
  );

  /**
   * Maneja el envío del formulario para crear una nueva categoría.
   * @param {Object} formData - Datos del formulario de la categoría.
   */
  const handleCreateCategory = async (formData) => {
    try {
      setIsSaving(true);
      setFormErrors({});

      const errors = {};
      if (!formData.name?.trim()) errors.name = "Nombre requerido";
      if (Object.keys(errors).length) {
        setFormErrors(errors);
        return;
      }

      await createCategory(formData);
      toast.success("Categoría creada correctamente");
      refetchCategories();
      setShowCreateForm(false);
    } catch (err) {
      setFormErrors({ form: err.message || "Error al crear la categoría" });
    } finally {
      setIsSaving(false);
    }
  };

  /**
   * Abre el modal de edición para una categoría específica.
   * @param {Object} category - La categoría a editar.
   */
  const handleEditCategory = (category) => {
    setEditingCategory(category);
    setFormErrors({});
  };

  /**
   * Maneja el envío del formulario para actualizar una categoría existente.
   * @param {Object} formData - Datos del formulario de la categoría.
   */
  const handleUpdateCategory = async (formData) => {
    try {
      setIsSaving(true);
      setFormErrors({});

      const errors = {};
      if (!formData.name?.trim()) errors.name = "Nombre requerido";
      if (Object.keys(errors).length) {
        setFormErrors(errors);
        return;
      }

      await updateCategory(editingCategory.id, formData);
      toast.success("Categoría actualizada correctamente");
      refetchCategories();
      setEditingCategory(null);
    } catch (err) {
      setFormErrors({
        form: err.message || "Error al actualizar la categoría",
      });
    } finally {
      setIsSaving(false);
    }
  };

  /**
   * Maneja la eliminación de una categoría tras una confirmación del usuario.
   * @param {string|number} id - El ID de la categoría a eliminar.
   */
  const handleDeleteCategory = async (id) => {
    setCategoryToDelete(id);
    setShowDeleteConfirm(true);
  };

  /**
   * Confirma y ejecuta la eliminación de la categoría.
   */
  const confirmDeleteCategory = async () => {
    if (categoryToDelete) {
      try {
        await deleteCategory(categoryToDelete);
        toast.success("Categoría eliminada correctamente");
        refetchCategories();
      } catch (err) {
        toast.error("Error al eliminar la categoría: " + err.message);
      } finally {
        setShowDeleteConfirm(false);
        setCategoryToDelete(null);
      }
    }
  };

  /**
   * Cancela la eliminación de la categoría.
   */
  const cancelDeleteCategory = () => {
    setShowDeleteConfirm(false);
    setCategoryToDelete(null);
  };

  /**
   * Maneja el cambio de estado (activo/inactivo) de una categoría.
   * @param {Object} category - La categoría cuyo estado se va a cambiar.
   */
  const handleToggleStatus = async (category) => {
    try {
      // El payload se envía como objeto, según el nuevo servicio
      await toggleCategoryStatus(category.id, { active: !category.active });
      toast.success(
        `Categoría ${
          category.active ? "desactivada" : "activada"
        } correctamente`
      );
      refetchCategories();
    } catch (err) {
      alert("Error al cambiar el estado: " + err.message);
    }
  };

  /**
   * Cierra el modal de creación/edición y resetea el estado del formulario.
   */
  const handleCloseForm = () => {
    setShowCreateForm(false);
    setEditingCategory(null);
    setFormErrors({});
  };

  // --- Estados de carga y error iniciales ---
  if (loading) {
    return <div className="loading">Cargando categorías...</div>;
  }

  if (error) {
    return <div className="error">Error: {error}</div>;
  }

  return (
    <div className="categories-list">
      {/* === Tarjetas de estadísticas === */}
      <div className="stats-grid">
        <div className="stat-card">
          <div>
            <p className="stat-label">Total Categorías</p>
            <p className="stat-value">{categories.length}</p>
          </div>
          <Package className="stat-icon" />
        </div>

        <div className="stat-card">
          <div>
            <p className="stat-label">Categorías Activas</p>
            <p className="stat-value text-green">{activeCategories}</p>
          </div>
          <CheckCircle className="stat-icon text-green" />
        </div>

        <div className="stat-card">
          <div>
            <p className="stat-label">Total Productos</p>
            <p className="stat-value">{totalProducts}</p>
          </div>
          <Package className="stat-icon" />
        </div>
      </div>

      {/* === Tabla de categorías === */}
      <div className="card">
        <div className="card-header-with-actions">
          <h2 className="card-title">Gestión de Categorías</h2>
          <div className="card-actions">
            <button
              type="button"
              className="reload-btn"
              aria-label="Recargar listado de categorías"
              title="Recargar"
              onClick={refetchCategories}
            >
              <RefreshCw className="reload-icon" size={16} />
            </button>
            <Button
              onClick={() => setShowCreateForm(true)}
              className="btn-primary"
            >
              <Plus size={16} className="mr-1" />
              Nueva Categoría
            </Button>
          </div>
        </div>

        <div className="table-wrapper">
          <Table className="table">
            <TableHeader>
              <TableRow>
                <TableHead>Nombre</TableHead>
                <TableHead>Descripción</TableHead>
                <TableHead>Productos</TableHead>
                <TableHead>Estado</TableHead>
                <TableHead className="text-right">Acciones</TableHead>
              </TableRow>
            </TableHeader>

            <TableBody>
              {currentCategories.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={5} className="text-center text-muted">
                    No hay categorías registradas
                  </TableCell>
                </TableRow>
              ) : (
                currentCategories.map((category) => (
                  <TableRow key={category.id}>
                    <TableCell>{category.name}</TableCell>
                    <TableCell>{category.description || "-"}</TableCell>
                    <TableCell>{category.productCount || 0}</TableCell>
                    <TableCell>
                      <div className="status-toggle">
                        <input
                          type="checkbox"
                          className="switch"
                          checked={category.active}
                          onChange={() => handleToggleStatus(category)}
                          disabled={togglingStatus}
                        />
                        <span
                          className={
                            category.active
                              ? "status-active"
                              : "status-inactive"
                          }
                        >
                          {category.active ? "Activa" : "Inactiva"}
                        </span>
                      </div>
                    </TableCell>

                    <TableCell className="text-right">
                      <button
                        className="btn-outline btn-sm mr-2"
                        onClick={() => handleEditCategory(category)}
                      >
                        <Edit size={16} className="mr-1" />
                      </button>
                      <button
                        className="btn-outline btn-sm btn-danger"
                        onClick={() => handleDeleteCategory(category.id)}
                        disabled={deletingCategory}
                      >
                        <Trash2 size={16} className="mr-1" />
                      </button>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>

          {/* === Paginación === */}
          {totalPages > 1 && (
            <div className="pagination">
              <button
                disabled={currentPage === 1}
                onClick={() => setCurrentPage((prev) => Math.max(1, prev - 1))}
              >
                Anterior
              </button>
              {Array.from({ length: totalPages }, (_, i) => (
                <button
                  key={i}
                  className={currentPage === i + 1 ? "active" : ""}
                  onClick={() => setCurrentPage(i + 1)}
                >
                  {i + 1}
                </button>
              ))}
              <button
                disabled={currentPage === totalPages}
                onClick={() =>
                  setCurrentPage((prev) => Math.min(totalPages, prev + 1))
                }
              >
                Siguiente
              </button>
            </div>
          )}
        </div>
      </div>

      {/* === Modal de creación/edición === */}
      {(showCreateForm || editingCategory) && (
        <div
          className="modal-overlay"
          onMouseDown={handleCloseForm}
          role="presentation"
        >
          <div
            onMouseDown={(e) => e.stopPropagation()}
            aria-modal="true"
            role="dialog"
            style={{ maxWidth: 720 }}
          >
            <CategoryForm
              category={editingCategory}
              onSave={
                editingCategory ? handleUpdateCategory : handleCreateCategory
              }
              onCancel={handleCloseForm}
              isSaving={isSaving}
              errors={formErrors}
            />
          </div>
        </div>
      )}

      {/* === Modal de confirmación de eliminación === */}
      {showDeleteConfirm && (
        <div
          className="modal-overlay"
          onMouseDown={cancelDeleteCategory}
          role="presentation"
        >
          <div
            className="modal-container"
            onMouseDown={(e) => e.stopPropagation()}
            aria-modal="true"
            role="alertdialog"
          >
            {/* Header */}
            <div className="modal-header">
              <h3 className="modal-title">¿Eliminar categoría?</h3>
              <p className="modal-subtitle">
                Esta acción no se puede deshacer. La categoría será eliminada
                permanentemente.
              </p>
            </div>

            {/* Footer */}
            <div className="modal-footer">
              <Button
                onClick={confirmDeleteCategory}
                disabled={deletingCategory}
                className={`btn btn-danger ${
                  deletingCategory ? "btn-disabled" : ""
                }`}
              >
                {deletingCategory ? "Eliminando..." : "Eliminar"}
              </Button>
              <Button
                onClick={cancelDeleteCategory}
                className="btn btn-secondary"
              >
                Cancelar
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
