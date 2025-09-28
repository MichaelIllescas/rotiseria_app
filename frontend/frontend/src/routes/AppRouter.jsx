import { UsersTabs } from "../modules/users/components/UsersTabs"
import { Route, Routes } from "react-router-dom"
import { ProtectedRoute } from "../routes/ProtectedRoute"
export const AppRoutes = () => {

     return (
    <Routes>
            <Route path="/UserPage" element={<ProtectedRoute element={<UsersTabs />} />} />
            <Route path="/" element={<ProtectedRoute element={<UsersTabs />} />} />

    </Routes>

     )
}