import '../src/styles/global.css'
import { UserHeader } from './modules/users/components/UserHeader'
import { UserRegistrationForm } from './modules/users/components/UserRegistrationForm'
import { UsersList } from './modules/users/components/UsersList'
import { UsersTabs } from './modules/users/components/UsersTabs'
import { AppRoutes } from './routes/AppRouter'
import { Toaster } from "./ui/toaster"
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';



export default function App() {
  return (
    <>
      <Toaster />
      <AppRoutes />
      

    </>)
}