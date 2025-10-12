import { UsersTabs } from "../modules/users/components/UsersTabs"
import { CategoriesPage } from "../modules/category/pages/CategoriesPage"   
import { Route, Routes } from "react-router-dom"
import { ProtectedRoute } from "../routes/ProtectedRoute"
import { useAuth } from "../context/AuthContext"
import  AdminPanel  from "../modules/users/pages/AdminUsersPanel"
import ProductsPage from "../modules/products/pages/ProductsPage"
import { BusinessPage } from "../modules/business/pages/BusinessPage"
import Dashboard from "../modules/common/Dashboard"
import HomePage from "../public/pages/HomePage"
import { CartPage } from "../public/pages/CartPage"
export const AppRoutes = () => {

//extraer el roler del user del context
        const { user } = useAuth();
        const userRole = user?.role;

     return (
    <Routes>

        {/* Rutas explusivas para el user DUENO */}
        {userRole === 'DUENO' && (
          <>
            <Route path="/users" element={<ProtectedRoute element={<AdminPanel />} />} />
            <Route path="/categories" element={<ProtectedRoute element={<CategoriesPage />} />} />
            <Route path="/products" element={<ProtectedRoute element={<ProductsPage />} />} />
            <Route path="/settings" element={<ProtectedRoute element={<BusinessPage />} />} />
            <Route path="/dashboard" element={<ProtectedRoute element={<Dashboard />} />} />
          </>
        )}
            
        <Route path="/"  element={<HomePage />} />
         <Route path="/cart" element={<CartPage />} />

    </Routes>

     )
}