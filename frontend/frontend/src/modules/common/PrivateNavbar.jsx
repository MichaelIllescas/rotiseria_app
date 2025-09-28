import { useAuth } from "../../context/AuthContext";
import { UserHeader } from "../users/components/UserHeader";


const PrivateNavbar = () => {
  const { user, logout } = useAuth();
  return (
    <div>
        <UserHeader currentUser={user} onLogout={logout} />
    </div>
  )
}

export default PrivateNavbar
