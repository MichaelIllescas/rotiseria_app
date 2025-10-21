import React, { useState } from "react";
import {
  Edit,
  Trash2,
  Plus,
  Package,
  RefreshCw,
  CheckCircle,
  DollarSign ,
  Image as ImageIcon,
  Check,
  X,
} from "lucide-react";
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableCell,
} from "../../../ui/table";
import { Button } from "../../../ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "../../../ui/card";
import { ProductForm } from "./ProductForm";
import { useProducts } from "../hooks/useProducts";
import { useCreateProduct } from "../hooks/useCreateProduct";
import { useUpdateProduct } from "../hooks/useUpdateProduct";
import { useDeleteProduct } from "../hooks/useDeleteProduct";
import { useToggleProductStatus } from "../hooks/useToggleProductStatus";
import { useCategories } from "../../category/hooks/useCategories"; // Necesitamos las categorías para el formulario
import { useUpdateProductStock } from "../hooks/useUpdateProductStock";
import { useStockUpdate } from "../hooks/useStockUpdate";
import "../styles/productsList.css";
import { ConfirmModal } from "./ConfirmModal";
import StockUpdateModal from "./StockUpdateModal";
const ITEMS_PER_PAGE = 5;

export function ProductsList() {
  const { products, loading, error, refetchProducts } = useProducts();
  const { createProduct } = useCreateProduct();
  const { updateProduct } = useUpdateProduct();
  const { deleteProduct } = useDeleteProduct();
  const { toggleProductStatus } = useToggleProductStatus();
  const { categories } = useCategories(); // Obtener categorías para el select
  const { updateProductStock } = useUpdateProductStock();
  const {
    isModalOpen: isStockModalOpen,
    stockData,
    isLoading: isStockLoading,
    hasChanges: hasStockChanges,
    openModal: openStockModal,
    closeModal: closeStockModal,
    updateStock,
    submitStockUpdates
  } = useStockUpdate();

  //urlbase de las imagenes de los productos
  const imageBaseUrl = "http://localhost:8080";

  const [currentPage, setCurrentPage] = useState(1);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  const [formErrors, setFormErrors] = useState({});
  const [isSaving, setIsSaving] = useState(false);
  const [previewImage, setPreviewImage] = useState(null);
  const [productToDelete, setProductToDelete] = useState(null);
  const [editingStock, setEditingStock] = useState(null);
  const [tempStock, setTempStock] = useState('');

  const handleImageClick = (url) => {
    setPreviewImage(url);
  };

  const handleClosePreview = () => {
    setPreviewImage(null);
  };

  const totalPages = Math.ceil(products.length / ITEMS_PER_PAGE);
  const currentProducts = products.slice(
    (currentPage - 1) * ITEMS_PER_PAGE,
    currentPage * ITEMS_PER_PAGE
  );

  const activeProducts = products.filter((p) => p.active).length;
  const totalStockValue = products.reduce(
    (sum, p) => sum + (p.price * p.dailyStock || 0),
    0
  );

  const handleCreateProduct = async (formData) => {
    try {
      setIsSaving(true);
      setFormErrors({});
      const errors = {};
      if (!formData.name?.trim()) errors.name = "Nombre requerido";
      if (!formData.categoryId) errors.categoryId = "Categoría requerida";
      if (!formData.description?.trim())
        errors.description = "Descripción requerida";
      if (!formData.price) errors.price = "Precio requerido";
      if (!formData.dailyStock) errors.dailyStock = "Stock diario requerido";

      if (Object.keys(errors).length) {
        setFormErrors(errors);
        return;
      }

      await createProduct(formData);
      refetchProducts();
      setShowCreateForm(false);
    } catch (err) {
      setFormErrors({ form: err.message });
    } finally {
      setIsSaving(false);
    }
  };

  const handleEditProduct = (product) => {
    setEditingProduct(product);
    setFormErrors({});
  };

  const handleUpdateProduct = async (formData) => {
    try {
      setIsSaving(true);
      setFormErrors({});
      const errors = {};
      if (!formData.name?.trim()) errors.name = "Nombre requerido";
      if (!formData.description?.trim())
        errors.description = "Descripción requerida";
      if (!formData.categoryId) errors.categoryId = "Categoría requerida";
      if (
        formData.price === "" ||
        formData.price === null ||
        formData.price === undefined
      ) {
        errors.price = "Precio requerido";
      }
      if (!formData.dailyStock) errors.dailyStock = "Stock diario requerido";
      if (Object.keys(errors).length) {
        setFormErrors(errors);
        return;
      }

      await updateProduct(editingProduct.id, formData);
      refetchProducts();
      setEditingProduct(null);
    } catch (err) {
      setFormErrors({ form: err.message });
    } finally {
      setIsSaving(false);
    }
  };

  const handleDeleteProduct = (id) => {
    setProductToDelete(id);
  };

  const confirmDelete = async () => {
    try {
      await deleteProduct(productToDelete);
      refetchProducts();
    } catch (err) {
      alert("Error al eliminar el producto: " + err.message);
    } finally {
      setProductToDelete(null);
    }
  };

  const handleToggleStatus = async (product) => {
    try {
      await toggleProductStatus(product.id);
      refetchProducts();
    } catch (err) {
      alert("Error al cambiar el estado: " + err.message);
    }
  };

  const handleCloseForm = () => {
    setShowCreateForm(false);
    setEditingProduct(null);
    setFormErrors({});
  };

  const handleStockDoubleClick = (product) => {
    setEditingStock(product.id);
    setTempStock(product.dailyStock.toString());
  };

  const handleStockSave = async (productId) => {
    try {
      const newStock = parseInt(tempStock);
      if (isNaN(newStock) || newStock < 0) {
        alert('Por favor ingrese un número válido');
        return;
      }
      
      await updateProductStock(productId, newStock);
      refetchProducts();
      setEditingStock(null);
      setTempStock('');
    } catch (err) {
      alert('Error al actualizar el stock: ' + err.message);
    }
  };

  const handleStockCancel = () => {
    setEditingStock(null);
    setTempStock('');
  };

  const handleStockKeyPress = (e, productId) => {
    if (e.key === 'Enter') {
      handleStockSave(productId);
    } else if (e.key === 'Escape') {
      handleStockCancel();
    }
  };

  const handleBulkStockUpdate = () => {
    openStockModal(products);
  };

  const handleSaveStockUpdates = async () => {
    try {
      const result = await submitStockUpdates();
      alert(`Stock actualizado correctamente para ${result.updatedCount} productos`);
      refetchProducts();
      closeStockModal();
    } catch (error) {
      alert('Error al actualizar el stock: ' + error.message);
    }
  };

  if (loading) return <div className="loading">Cargando productos...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <>
      <div className="products-list">
        {/* === Tarjetas de stats === */}
        <div className="stats-grid">
          <div className="stat-card">
            <div>
              <p className="stat-label">Total Productos</p>
              <p className="stat-value">{products.length}</p>
            </div>
            <Package className="stat-icon" />
          </div>
          <div className="stat-card">
            <div>
              <p className="stat-label">Productos Activos</p>
              <p className="stat-value text-green">{activeProducts}</p>
            </div>
            <CheckCircle className="stat-icon text-green" />
          </div>
          <div className="stat-card">
            <div>
              <p className="stat-label">Valor en Stock</p>
              <p className="stat-value">${totalStockValue.toFixed(2)}</p>
            </div>
            <DollarSign  className="stat-icon" />
          </div>
        </div>

        {/* === Tabla de productos === */}
        <Card>
          <div className="card-header-with-reload">
            <h2 className="card-title">Gestión de Productos</h2>
            <div className="card-actions">
              <button
                type="button"
                className="reload-btn"
                onClick={refetchProducts}
              >
                <RefreshCw className="reload-icon" size={16} />
              </button>
              <Button
                onClick={() => setShowCreateForm(true)}
                className="btn-primary"
              >
                <Plus size={16} className="mr-1" /> Agregar Producto
              </Button>
              <button
                onClick={handleBulkStockUpdate}
                className="update-stock-btn"
                disabled={products.length === 0}
              >
                <RefreshCw size={16} /> Actualizar Stock
              </button>
            </div>
          </div>
          <div className="table-wrapper table-responsive">
            <Table className="table table-striped table-hover table-responsive">
              <TableHeader>
                <TableRow>
                  <TableHead>Imagen</TableHead>
                  <TableHead>Producto</TableHead>
                  <TableHead>Categoría</TableHead>
                  <TableHead>Precio</TableHead>
                  <TableHead>Stock</TableHead>
                  <TableHead>Estado</TableHead>
                  <TableHead className="text-right">Acciones</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {currentProducts.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={7} className="text-center text-muted">
                      No hay productos registrados
                    </TableCell>
                  </TableRow>
                ) : (
                  currentProducts.map((product) => (
                    <TableRow key={product.id}>
                      <TableCell className="image-cell-table">
                        {product.imageUrl ? (
                          <img
                            src={`${imageBaseUrl}${product.imageUrl}`}
                            alt={product.name}
                            style={{
                              width: "40px",
                              height: "40px",
                              objectFit: "cover",
                              borderRadius: "4px",
                            }}
                            onClick={() =>
                              handleImageClick(
                                `${imageBaseUrl}${product.imageUrl}`
                              )
                            }
                          />
                        ) : (
                          <ImageIcon className="text-muted" size={24} />
                        )}
                      </TableCell>
                      <TableCell>{product.name}</TableCell>
                      <TableCell>
                        {categories.find((c) => c.id === product.categoryId)
                          ?.name || "N/A"}
                      </TableCell>
                      <TableCell>
                        ${parseFloat(product.price).toFixed(2)}
                      </TableCell>
                      <TableCell>
                        <div className="stock-cell">
                          {editingStock === product.id ? (
                            <div className="stock-edit-container">
                              <input
                                type="number"
                                value={tempStock}
                                onChange={(e) => setTempStock(e.target.value)}
                                onKeyDown={(e) => handleStockKeyPress(e, product.id)}
                                className="stock-input"
                                autoFocus
                                min="0"
                              />
                              <div className="stock-edit-buttons">
                                <button
                                  onClick={() => handleStockSave(product.id)}
                                  className="stock-save-btn"
                                  title="Guardar"
                                >
                                  <Check size={14} />
                                </button>
                                <button
                                  onClick={handleStockCancel}
                                  className="stock-cancel-btn"
                                  title="Cancelar"
                                >
                                  <X size={14} />
                                </button>
                              </div>
                            </div>
                          ) : (
                            <div 
                              className="stock-display"
                              onDoubleClick={() => handleStockDoubleClick(product)}
                              title="Doble clic para editar"
                            >
                              <span>{product.dailyStock}</span>
                              <Edit 
                                size={14} 
                                className="stock-edit-icon"
                                onClick={() => handleStockDoubleClick(product)}
                              />
                            </div>
                          )}
                        </div>
                      </TableCell>
                      <TableCell>
                        <div className="status-toggle">
                          <input
                            type="checkbox"
                            className="switch"
                            checked={product.active}
                            onChange={() => handleToggleStatus(product)}
                          />
                          <span
                            className={
                              product.active
                                ? "status-active"
                                : "status-inactive"
                            }
                          >
                            {product.active ? "Activo" : "Inactivo"}
                          </span>
                        </div>
                      </TableCell>
                      <TableCell className="text-right">
                        <button
                          className="btn-outline btn-sm mr-2"
                          onClick={() => handleEditProduct(product)}
                        >
                          <Edit size={16} className="mr-1" />
                        </button>
                        <button
                          className="btn-outline btn-sm btn-danger"
                          onClick={() => handleDeleteProduct(product.id)}
                        >
                          <Trash2 size={16} className="mr-1" />{" "}
                        </button>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
            {totalPages > 1 && (
              <div className="pagination">
                <button
                  disabled={currentPage === 1}
                  onClick={() => setCurrentPage((p) => Math.max(1, p - 1))}
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
                    setCurrentPage((p) => Math.min(totalPages, p + 1))
                  }
                >
                  Siguiente
                </button>
              </div>
            )}
          </div>
        </Card>
      </div>

      {/* === Modal de creación/edición === */}
      {(showCreateForm || editingProduct) && (
        <div
          className="product-form-overlay"
          onMouseDown={handleCloseForm}
          role="presentation"
        >
          <div
            className="product-form-container"
            onMouseDown={(e) => e.stopPropagation()}
            aria-modal="true"
            role="dialog"
          >
            <ProductForm
              product={editingProduct}
              categories={categories}
              onSave={
                editingProduct ? handleUpdateProduct : handleCreateProduct
              }
              onCancel={handleCloseForm}
              isSaving={isSaving}
              errors={formErrors}
              imageUrlBaseUrl={imageBaseUrl}
            />
          </div>
        </div>
      )}
      {previewImage && (
        <div
          className="image-preview-overlay"
          onClick={handleClosePreview}
          role="presentation"
        >
          <div
            className="image-preview-container"
            onClick={(e) => e.stopPropagation()}
          >
            <img
              src={previewImage}
              alt="Vista ampliada"
              className="image-preview-full"
            />
            <button
              className="image-preview-close"
              onClick={handleClosePreview}
            >
              ✕
            </button>
          </div>
        </div>
      )}

      {productToDelete && (
        <ConfirmModal
          title="Confirmar eliminación"
          message="¿Estás seguro de que deseas eliminar este producto?"
          onConfirm={confirmDelete}
          onCancel={() => setProductToDelete(null)}
        />
      )}

      {/* === Modal de actualización de stock === */}
      <StockUpdateModal
        isOpen={isStockModalOpen}
        onClose={closeStockModal}
        stockData={stockData}
        onStockChange={updateStock}
        onSave={handleSaveStockUpdates}
        isLoading={isStockLoading}
        hasChanges={hasStockChanges}
      />
    </>
  );
}
