import { UsersTabs } from "../components/UsersTabs"
import '../styles/usersPage.css'

const AdminUsersPanel = () => {
  return (
    <div className="users-page">
      <h1 className="users-page_title">Panel de Administración de Usuarios</h1>
      <p className="users-page_description">Aquí puedes gestionar los usuarios del sistema.</p>
    <UsersTabs />
    </div>
  )
}

export default AdminUsersPanel

