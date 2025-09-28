export const ROLE_VALUES = ["DUENO", "ENCARGADO", "ATENCION"];

export function validate(form) {
  const errors = {};

  if (!form.name || !form.name.trim()) errors.name = "El nombre es obligatorio.";
  else if (form.name.trim().length < 2) errors.name = "Al menos 2 caracteres.";

  if (!form.lastname || !form.lastname.trim()) errors.lastname = "El apellido es obligatorio.";
  else if (form.lastname.trim().length < 2) errors.lastname = "Al menos 2 caracteres.";

  if (!form.email || !form.email.trim()) errors.email = "El correo es obligatorio.";
  else {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!re.test(form.email.trim())) errors.email = "Correo inválido.";
  }

  if (!form.password) errors.password = "La contraseña es obligatoria.";
  else {
    if (form.password.length < 8) errors.password = "Al menos 8 caracteres.";
    const hasLetter = /[A-Za-z]/.test(form.password);
    const hasNumber = /[0-9]/.test(form.password);
    if (!hasLetter || !hasNumber) errors.password = "Debe contener letras y números.";
  }

  if (!form.role) errors.role = "El rol es obligatorio.";
  else if (!ROLE_VALUES.includes(form.role)) errors.role = "Rol inválido.";

  return { valid: Object.keys(errors).length === 0, errors };
}

export function validateField(name, value, fullForm = {}) {
  return validate({ ...fullForm, [name]: value });
}