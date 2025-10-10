import React from 'react';
import {
  TrendingUp,
  TrendingDown,
  DollarSign,
  ShoppingCart,
  Users,
  Package,
  Calendar,
  Clock,
  AlertTriangle,
  CheckCircle,
  Eye,
  Star
} from 'lucide-react';
import './styles/Dashboard.css';

const Dashboard = () => {
  return (
    <div className="dashboard">
      <div className="dashboard-container">
        <div className="dashboard-header">
          <h1 className="dashboard-title">Panel de Control</h1>
          <p className="dashboard-subtitle">Resumen general del negocio</p>
        </div>

      {/* Métricas principales */}
      <div className="metrics-grid">
        <div className="metric-card revenue">
          <div className="metric-content">
            <div className="metric-info">
              <h3 className="metric-value">$15,420</h3>
              <p className="metric-label">Ventas del Día</p>
              <div className="metric-change positive">
                <TrendingUp size={16} />
                <span>+12.5% vs ayer</span>
              </div>
            </div>
            <div className="metric-icon">
              <DollarSign size={32} />
            </div>
          </div>
        </div>

        <div className="metric-card orders">
          <div className="metric-content">
            <div className="metric-info">
              <h3 className="metric-value">847</h3>
              <p className="metric-label">Órdenes del Día</p>
              <div className="metric-change positive">
                <TrendingUp size={16} />
                <span>+8.2% vs ayer</span>
              </div>
            </div>
            <div className="metric-icon">
              <ShoppingCart size={32} />
            </div>
          </div>
        </div>

    

        <div className="metric-card products">
          <div className="metric-content">
            <div className="metric-info">
              <h3 className="metric-value">156</h3>
              <p className="metric-label">Productos Activos</p>
              <div className="metric-change neutral">
                <span>Sin cambios</span>
              </div>
            </div>
            <div className="metric-icon">
              <Package size={32} />
            </div>
          </div>
        </div>
      </div>

      {/* Gráficos y estadísticas adicionales */}
      <div className="charts-grid">
        {/* Ventas por categoría */}
        <div className="chart-card">
          <div className="chart-header">
            <h3 className="chart-title">Ventas por Categoría (Hoy)</h3>
          </div>
          <div className="chart-content">
            <div className="category-list">
              <div className="category-item">
                <div className="category-info">
                  <span className="category-name">Empanadas</span>
                  <span className="category-percentage">45%</span>
                </div>
                <div className="category-bar">
                  <div className="category-fill" style={{ width: '45%' }}></div>
                </div>
                <span className="category-value">$6,939</span>
              </div>
              
              <div className="category-item">
                <div className="category-info">
                  <span className="category-name">Pizzas</span>
                  <span className="category-percentage">30%</span>
                </div>
                <div className="category-bar">
                  <div className="category-fill" style={{ width: '30%' }}></div>
                </div>
                <span className="category-value">$4,626</span>
              </div>
              
              <div className="category-item">
                <div className="category-info">
                  <span className="category-name">Bebidas</span>
                  <span className="category-percentage">15%</span>
                </div>
                <div className="category-bar">
                  <div className="category-fill" style={{ width: '15%' }}></div>
                </div>
                <span className="category-value">$2,313</span>
              </div>
              
              <div className="category-item">
                <div className="category-info">
                  <span className="category-name">Postres</span>
                  <span className="category-percentage">10%</span>
                </div>
                <div className="category-bar">
                  <div className="category-fill" style={{ width: '10%' }}></div>
                </div>
                <span className="category-value">$1,542</span>
              </div>
            </div>
          </div>
        </div>

        {/* Horarios de mayor actividad */}
        <div className="chart-card">
          <div className="chart-header">
            <h3 className="chart-title">Horarios de Mayor Actividad</h3>
          </div>
          <div className="chart-content">
            <div className="time-slots">
              <div className="time-slot high">
                <Clock size={20} />
                <div className="time-info">
                  <span className="time-range">12:00 - 14:00</span>
                  <span className="time-orders">284 órdenes</span>
                </div>
              </div>
              
              <div className="time-slot medium">
                <Clock size={20} />
                <div className="time-info">
                  <span className="time-range">19:00 - 22:00</span>
                  <span className="time-orders">195 órdenes</span>
                </div>
              </div>
              
              <div className="time-slot low">
                <Clock size={20} />
                <div className="time-info">
                  <span className="time-range">08:00 - 11:00</span>
                  <span className="time-orders">128 órdenes</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Panel de alertas y productos destacados */}
      <div className="bottom-grid">
        {/* Alertas */}
        <div className="alert-card">
          <div className="alert-header">
            <h3 className="alert-title">Alertas de Stock</h3>
          </div>
          <div className="alert-content">
            <div className="alert-item warning">
              <AlertTriangle size={20} />
              <div className="alert-info">
                <span className="alert-product">Empanada de Carne</span>
                <span className="alert-message">Stock bajo: 12 unidades</span>
              </div>
            </div>
            
            <div className="alert-item warning">
              <AlertTriangle size={20} />
              <div className="alert-info">
                <span className="alert-product">Pizza Muzzarella</span>
                <span className="alert-message">Stock bajo: 8 unidades</span>
              </div>
            </div>
            
            <div className="alert-item success">
              <CheckCircle size={20} />
              <div className="alert-info">
                <span className="alert-product">Coca Cola 500ml</span>
                <span className="alert-message">Stock reabastecido</span>
              </div>
            </div>
          </div>
        </div>

        {/* Productos más vendidos */}
        <div className="top-products-card">
          <div className="products-header">
            <h3 className="products-title">Productos Más Vendidos</h3>
          </div>
          <div className="products-content">
            <div className="product-item">
              <div className="product-rank">1</div>
              <div className="product-info">
                <span className="product-name">Empanada de Carne</span>
                <span className="product-sales">127 vendidas</span>
              </div>
              <div className="product-revenue">$2,540</div>
            </div>
            
            <div className="product-item">
              <div className="product-rank">2</div>
              <div className="product-info">
                <span className="product-name">Pizza Napolitana</span>
                <span className="product-sales">89 vendidas</span>
              </div>
              <div className="product-revenue">$2,225</div>
            </div>
            
            <div className="product-item">
              <div className="product-rank">3</div>
              <div className="product-info">
                <span className="product-name">Empanada de Pollo</span>
                <span className="product-sales">76 vendidas</span>
              </div>
              <div className="product-revenue">$1,520</div>
            </div>
            
            <div className="product-item">
              <div className="product-rank">4</div>
              <div className="product-info">
                <span className="product-name">Coca Cola 500ml</span>
                <span className="product-sales">64 vendidas</span>
              </div>
              <div className="product-revenue">$960</div>
            </div>
          </div>
        </div>

        {/* Resumen rápido */}
        <div className="quick-stats-card">
          <div className="quick-header">
            <h3 className="quick-title">Resumen Rápido</h3>
          </div>
          <div className="quick-content">
            <div className="quick-stat">
              <Eye size={18} />
              <div className="quick-info">
                <span className="quick-value">1,247</span>
                <span className="quick-label">Visualizaciones del menú</span>
              </div>
            </div>
            
            <div className="quick-stat">
              <Star size={18} />
              <div className="quick-info">
                <span className="quick-value">4.8</span>
                <span className="quick-label">Calificación promedio</span>
              </div>
            </div>
            
            <div className="quick-stat">
              <Calendar size={18} />
              <div className="quick-info">
                <span className="quick-value">28</span>
                <span className="quick-label">Días consecutivos abierto</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      </div> {/* Cierre del dashboard-container */}
    </div>
  );
};

export default Dashboard;
