import { UsersTabs } from "../modules/users/components/UsersTabs"
import { Route, Routes } from "react-router-dom"
import { ProtectedRoute } from "../routes/ProtectedRoute"
import { useAuth } from "../context/AuthContext"
export const AppRoutes = () => {

//extraer el roler del user del context
        const { user } = useAuth();
        const userRole = user?.role;

     return (
    <Routes>
           //rutas apra el user DUENO
                  {userRole === 'DUENO' && (
            <Route path="/UserPage" element={<ProtectedRoute element={<UsersTabs />} />} />
            
        )}
            
        <Route path="/" element={<ProtectedRoute element={<UsersTabs />} />} />

    </Routes>

     )
}