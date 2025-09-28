import { Tabs, TabsList, TabsTrigger, TabsContent } from "@/components/ui/tabs";
import { UserStatsCards } from "../components/UserStatsCards";
import { UsersList } from "../components/UsersList";
import { UserRegistrationForm } from "../components/UserRegistrationForm";
export function AdminPanel() {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold">Panel de Administración</h1>
      <p className="text-gray-500">
        Gestiona los usuarios del sistema, asigna roles y controla accesos
      </p>

      <Tabs defaultValue="list" className="mt-6">
        <TabsList>
          <TabsTrigger value="list">Listado de Usuarios</TabsTrigger>
          <TabsTrigger value="register">Registrar Usuario</TabsTrigger>
        </TabsList>

        <TabsContent value="list">
          <UserStatsCards />
          <UsersList />
        </TabsContent>

        <TabsContent value="register">
          <UserRegistrationForm />
        </TabsContent>
      </Tabs>
    </div>
  );
}
