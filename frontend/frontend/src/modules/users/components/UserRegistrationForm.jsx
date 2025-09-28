import { useState } from "react";
import { Button } from "../../../ui/button";
import { Input } from "../../../ui/input";
import { Label } from "../../../ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../../../ui/select";
import { Card, CardContent, CardHeader, CardTitle } from "../../../ui/card";
import { UserPlus } from "lucide-react";
import "../styles/form.css";
import { validate } from "../helpers/validateForm";

const roleOptions = [
  { value: "DUENO", label: "Dueño" },
  { value: "ENCARGADO", label: "Encargado" },
  { value: "ATENCION", label: "Atención" },
];

export function UserRegistrationForm({ onSubmit }) {
  const [formData, setFormData] = useState({
    name: "",
    lastname: "",
    email: "",
    password: "",
    role: "ATENCION",
  });
  const [isLoading, setIsLoading] = useState(false);
  const [formErrors, setFormErrors] = useState({});

  const handleFieldChange = (field, value) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    setFormErrors((prev) => {
      if (!prev || !prev[field]) return prev;
      const next = { ...prev };
      delete next[field];
      return next;
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { valid, errors } = validate(formData);
    setFormErrors(errors);
    if (!valid) return;

    setIsLoading(true);
    try {
      await onSubmit(formData);
      setFormData({
        name: "",
        lastname: "",
        email: "",
        password: "",
        role: "ATENCION",
      });
      setFormErrors({});
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Card className="card">
      <CardHeader className="card-header">
        <CardTitle className="card-title">
          <UserPlus className="h-5 w-5" />
          Registrar Nuevo Usuario
        </CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4" noValidate>
          <div className="form-grid">
            <div>
              <Label htmlFor="name">Nombre</Label>
              <Input
                id="name"
                type="text"
                value={formData.name}
                onChange={(e) => handleFieldChange("name", e.target.value)}
                placeholder="Ingrese el nombre"
                required
              />
              {formErrors.name && (
                <small className="text-red-600 block mt-1">
                  {formErrors.name}
                </small>
              )}
            </div>
            <div>
              <Label htmlFor="lastname">Apellido</Label>
              <Input
                id="lastname"
                type="text"
                value={formData.lastname}
                onChange={(e) => handleFieldChange("lastname", e.target.value)}
                placeholder="Ingrese el apellido"
                required
              />
              {formErrors.lastname && (
                <small className="text-red-600 block mt-1">
                  {formErrors.lastname}
                </small>
              )}
            </div>
          </div>

          <div>
            <Label htmlFor="email">Correo Electrónico</Label>
            <Input
              id="email"
              type="email"
              value={formData.email}
              onChange={(e) => handleFieldChange("email", e.target.value)}
              placeholder="ejemplo@correo.com"
              required
            />
            {formErrors.email && (
              <small className="text-red-600 block mt-1">
                {formErrors.email}
              </small>
            )}
          </div>

          <div>
            <Label htmlFor="password">Contraseña</Label>
            <Input
              id="password"
              type="password"
              value={formData.password}
              onChange={(e) => handleFieldChange("password", e.target.value)}
              placeholder="Ingrese una contraseña segura"
              required
            />
            {formErrors.password && (
              <small className="text-red-600 block mt-1">
                {formErrors.password}
              </small>
            )}
          </div>

          <div>
            <Label htmlFor="role">Rol</Label>
            <Select
              value={formData.role}
              onValueChange={(value) => handleFieldChange("role", value)}
            >
              <SelectTrigger>
                <SelectValue placeholder="Seleccione un rol" />
              </SelectTrigger>
              <SelectContent className="select-content">
                {roleOptions.map((option) => (
                  <SelectItem
                    key={option.value}
                    value={option.value}
                    className="select-item"
                  >
                    {option.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            {formErrors.role && (
              <small className="text-red-600 block mt-1">
                {formErrors.role}
              </small>
            )}
          </div>

          <Button type="submit" className="w-full" disabled={isLoading}>
            {isLoading ? "Registrando..." : "Registrar Usuario"}
          </Button>
        </form>
      </CardContent>
    </Card>
  );
}
