import { useState, useEffect } from "react";
import { UserCog, LogOut, ChevronDown, Eye, EyeOff } from "lucide-react";
import "../styles/header.css";
import useUpdateUser from "../hooks/useUpdateUser";
import {useAuth} from '../../../context/AuthContext';
import useChangePassword from "../hooks/useChangePassword";

export function UserHeader({ currentUser, onUpdateProfile, onLogout }) {
	// estados de UI
	const [isProfileDialogOpen, setIsProfileDialogOpen] = useState(false);
	const [isDropdownOpen, setIsDropdownOpen] = useState(false);
	const [showLogoutConfirm, setShowLogoutConfirm] = useState(false);
  const { changePassword } = useChangePassword();

	// obtener user del contexto; preferimos mostrarlo frente a la prop si existe
	// Eliminado getUserSession porque no se usaba en el componente.
	const { setUser, user: authUser } = useAuth();
	const displayUser = authUser ?? currentUser;

	// ---- New state for editable form ----
	const [formData, setFormData] = useState({
		name: "",
		lastname: "",
		email: "",
		role: "", // se mantiene para garantizar envío del role al actualizar aunque no sea editable
	});
	const [isSaving, setIsSaving] = useState(false);
	const [saveError, setSaveError] = useState(null);
	// Eliminado saveSuccess / setSaveSuccess porque no se mostraba en la UI ni se usaba.

	// obtener la función updateUser del hook (no llamar al hook como función)
	const { updateUser } = useUpdateUser();

	// ---- New: estado y control para modal de cambio de contraseña (visual solo) ----
	const [isPasswordDialogOpen, setIsPasswordDialogOpen] = useState(false);
	const [passwordForm, setPasswordForm] = useState({
		currentPassword: "",
		newPassword: "",
		confirmPassword: "",
	});
	// estados para mostrar/ocultar cada campo de contraseña
	const [showCurrentPassword, setShowCurrentPassword] = useState(false);
	const [showNewPassword, setShowNewPassword] = useState(false);
	const [showConfirmPassword, setShowConfirmPassword] = useState(false);
	const [passwordError, setPasswordError] = useState(null);
	const [isChangingPassword, setIsChangingPassword] = useState(false);

	/**
	 * getInitials(name, lastname)
	 * - Qué hace: devuelve las iniciales formadas por la primera letra de name y lastname en mayúsculas.
	 * - Qué no hace: no maneja middle names ni nombres compuestos (solo usa la primera letra de cada campo).
	 * - Caso fallback: si falta name o lastname devuelve "?".
	 */
	const getInitials = (name, lastname) => {
		if (!name || !lastname) return "?";
		return `${name.charAt(0)}${lastname.charAt(0)}`.toUpperCase();
	};

	/**
	 * handleOpenProfile()
	 * - Qué hace: cierra el dropdown y abre el diálogo de perfil.
	 * - Qué no hace: no carga datos adicionales del servidor; sólo abre la UI para editar los datos que ya están en memoria.
	 */
	const handleOpenProfile = () => {
		setIsDropdownOpen(false);
		setIsProfileDialogOpen(true);
	};

	/**
	 * Efecto: al abrir el diálogo de perfil o cuando cambia el usuario mostrado,
	 * - Qué hace: popula los inputs del formulario desde displayUser.
	 * - Qué no hace: no valida la consistencia con el servidor ni sincroniza automáticamente (solo inicializa los inputs).
	 */
	useEffect(() => {
		if (isProfileDialogOpen && displayUser) {
			setFormData({
				name: displayUser.name || "",
				lastname: displayUser.lastname || "",
				email: displayUser.email || "",
				role: displayUser.role || "ATENCION",
			});
			setSaveError(null);
			// Nota: antes se limpiaba saveSuccess aquí; se eliminó porque no se mostraba.
		}
	}, [isProfileDialogOpen, displayUser]);

	/**
	 * handleChange(e)
	 * - Qué hace: actualiza formData para los inputs controlados.
	 * - Qué no hace: no realiza validación compleja ni normalización de campos (solo asigna el valor tal cual).
	 */
	const handleChange = (e) => {
		const { name, value } = e.target;
		setFormData((s) => ({ ...s, [name]: value }));
	};

	/**
	 * handleSaveProfile(e)
	 * - Qué hace:
	 *   1. Previene el submit por defecto y muestra estado de guardado.
	 *   2. Valida presencia básica de nombre, apellido y email.
	 *   3. Construye un payload y llama a updateUser (hook) o al callback onUpdateProfile si no hay hook.
	 *   4. Actualiza el contexto con el usuario devuelto (setUser) y notifica al padre via onUpdateProfile si existe.
	 *   5. Cierra el modal tras una breve demora.
	 * - Qué no hace:
	 *   - No maneja validaciones avanzadas de email ni formateo.
	 *   - No muestra un mensaje de éxito persistente dentro del componente (antes existía saveSuccess pero se eliminó porque no se usaba).
	 */
	const handleSaveProfile = async (e) => {
		e.preventDefault();
		setIsSaving(true);
		setSaveError(null);

		// simple validation
		if (
			!formData.name?.trim() ||
			!formData.lastname?.trim() ||
			!formData.email?.trim()
		) {
			setSaveError("Nombre, apellido y email son requeridos.");
			setIsSaving(false);
			return;
		}

		try {
			const payload = {
				// si el currentUser tiene id, incluirlo para el backend
				id: currentUser?.id,
				name: formData.name,
				lastname: formData.lastname,
				email: formData.email,
				// asegurar que role siempre se mande: preferir formData, luego currentUser, luego fallback
				role: formData.role ?? currentUser?.role ?? "ATENCION",
			};

			// ejecutar el update: preferir hook, fallback a onUpdateProfile (si es función)
			let result;
			if (typeof updateUser === "function") {
				result = await updateUser(payload);
			} else if (typeof onUpdateProfile === "function") {
				result = await onUpdateProfile(payload);
			} else {
				throw new Error("No hay handler disponible para actualizar el perfil");
			}

			// normalizar la respuesta
			const updatedUser = result?.user ?? result?.data ?? result ?? payload;

			// actualizar el context inmediatamente con el usuario devuelto
			try {
				setUser(updatedUser);
			} catch (e ) {
				/* noop */
        console.error(e);
			}

			// notificar al padre si se proporcionó la función (opcional)
			if (typeof onUpdateProfile === "function") {
				try {
					onUpdateProfile(updatedUser);
				} catch (_e) {
					/* noop */
          console.error(_e);
				}
			}

			// cerrar modal tras breve demora para que el usuario vea el mensaje de éxito
			setTimeout(() => {
				setIsProfileDialogOpen(false);
				setIsSaving(false);
			}, 800);
		} catch (err) {
			setSaveError(err?.message || "Error al actualizar el perfil.");
			setIsSaving(false);
		}
	};

	/**
	 * handleOpenLogout()
	 * - Qué hace: cierra el dropdown y abre la confirmación de cierre de sesión.
	 * - Qué no hace: no realiza el logout; solo muestra el modal de confirmación.
	 */
	const handleOpenLogout = () => {
		setIsDropdownOpen(false);
		setShowLogoutConfirm(true);
	};

	/**
	 * handleConfirmLogout()
	 * - Qué hace: cierra la confirmación y llama al callback onLogout proporcionado por el padre.
	 * - Qué no hace: no intenta limpiar el contexto local ni redirigir (eso debe manejarlo quien implemente onLogout).
	 */
	const handleConfirmLogout = () => {
		setShowLogoutConfirm(false);
		onLogout();
	};

	/**
	 * handleOpenChangePassword()
	 * - Abre el modal de cambio de contraseña (visual).
	 * - No realiza ninguna llamada al servidor.
	 */
	const handleOpenChangePassword = () => {
		setIsDropdownOpen(false);
		setPasswordError(null);
		setPasswordForm({
			currentPassword: "",
			newPassword: "",
			confirmPassword: "",
		});
		setIsPasswordDialogOpen(true);
	};

	/**
	 * handlePasswordInputChange(e)
	 * - Actualiza los campos del formulario de contraseña.
	 */
	const handlePasswordInputChange = (e) => {
		const { name, value } = e.target;
		setPasswordForm((s) => ({ ...s, [name]: value }));
	};

	/**
	 * handleChangePasswordSubmit(e)
	 * - Validación cliente (visual únicamente):
	 *   - Todos los campos requeridos.
	 *   - newPassword debe coincidir con confirmPassword.
	 * - No realiza petición al backend; en caso de éxito simplemente cierra el modal tras breve delay.
	 */
	const handleChangePasswordSubmit = async (e) => {
        e.preventDefault();
        setPasswordError(null);

        const { currentPassword, newPassword, confirmPassword } = passwordForm;

        if (!currentPassword?.trim() || !newPassword?.trim() || !confirmPassword?.trim()) {
            setPasswordError("Todos los campos son requeridos.");
            return;
        }

        if (newPassword !== confirmPassword) {
            setPasswordError("La nueva contraseña y su confirmación no coinciden.");
            return;
        }

        // iniciar indicador de carga
        setIsChangingPassword(true);

        try {
            // construir payload exactamente en el formato que requiere el backend
            const payload = {
                currentPassword: passwordForm.currentPassword,
                newPassword: passwordForm.newPassword,
            };

            // enviar al hook/backend (se mantiene el id si tu hook lo requiere como primer argumento)
            // ajusta la llamada si tu hook espera sólo el payload.
            const result = await changePassword(currentUser?.id, payload);

            // manejar formatos de respuesta comunes: si el hook/backend devuelve error sin lanzar excepción
            const errorMessage =
                result?.error ||
                result?.message ||
                (result?.ok === false && (result?.data?.message || "Error al cambiar la contraseña."));

            if (errorMessage) {
                setPasswordError(errorMessage);
                setIsChangingPassword(false);
                // NO cerrar el modal: permitir que el usuario corrija
                return;
            }

            // éxito: limpiar formulario y cerrar modal tras breve delay para feedback visual
            setTimeout(() => {
                setIsChangingPassword(false);
                setIsPasswordDialogOpen(false);
                setPasswordForm({
                    currentPassword: "",
                    newPassword: "",
                    confirmPassword: "",
                });
            }, 800);
        } catch (err) {
            // si changePassword lanza excepción
            setPasswordError(err?.message || "Error al cambiar la contraseña.");
            setIsChangingPassword(false);
            // NO cerrar el modal
        }
    };

	return (
		<div className="header">
			{/* Título */}
			<h1 className="header-title">Panel de Administración</h1>

			{/* Botón avatar */}
			<div className="relative">
				<button
					className="user-button"
					onClick={() => setIsDropdownOpen(!isDropdownOpen)}
				>
					<div className="avatar">
						{displayUser
							? getInitials(displayUser.name, displayUser.lastname)
							: "?"}
					</div>
					<ChevronDown />
				</button>

				{/* Dropdown */}
				{isDropdownOpen && (
					<>
						<div className="dropdown">
							<div className="dropdown-header">
								{displayUser && (
									<>
										<div className="name">
											{displayUser.name} {displayUser.lastname}
										</div>
										<div className="email">{displayUser.email}</div>
									</>
								)}
							</div>

							<div className="py-1">
								<button className="dropdown-item" onClick={handleOpenProfile}>
									<UserCog /> Mi Perfil
								</button>

								{/* Nuevo botón para abrir modal de cambio de contraseña (visual) */}
								<button className="dropdown-item" onClick={handleOpenChangePassword}>
									{/* Reutiliza icono de UserCog para consistencia visual */}
									<UserCog style={{ opacity: 0.9 }} /> Cambiar Contraseña
								</button>

								<hr className="border" />

								<button
									className="dropdown-item logout"
									onClick={handleOpenLogout}
								>
									<LogOut /> Cerrar Sesión
								</button>
							</div>
						</div>

						{/* Overlay para cerrar el dropdown */}
						<div className="overlay" onClick={() => setIsDropdownOpen(false)} />
					</>
				)}
			</div>

			{/* Modal confirmación logout */}
			{showLogoutConfirm && (
				<div className="modal-overlay">
					<div className="modal">
						<h3>¿Cerrar Sesión?</h3>
						<p>
							¿Estás seguro de que deseas cerrar sesión? Tendrás que iniciar
							sesión nuevamente para acceder al panel.
						</p>
						<div className="modal-actions">
							<button
								className="action-cancel"
								onClick={() => setShowLogoutConfirm(false)}
							>
								Cancelar
							</button>
							<button className="action-logout" onClick={handleConfirmLogout}>
								Cerrar Sesión
							</button>
						</div>
					</div>
				</div>
			)}

			{/* Dialog de perfil con inputs editables */}
			{isProfileDialogOpen && (
				<div className="modal-overlay">
					<div className="modal">
						<h3>Mi Perfil</h3>
						<form onSubmit={handleSaveProfile} className="profile-form">
							<label>
								Nombre
								<input
									name="name"
									value={formData.name}
									onChange={handleChange}
									type="text"
									// no autoFocus to avoid selecting text
								/>
							</label>

							<label>
								Apellido
								<input
									name="lastname"
									value={formData.lastname}
									onChange={handleChange}
									type="text"
								/>
							</label>

							<label>
								Email
								<input
									name="email"
									value={formData.email}
									onChange={handleChange}
									type="email"
								/>
							</label>

							{/* optional messages */}
							{saveError && <div className="form-error error-message small.error-message">{saveError}</div>}

							<div className="modal-actions">
								<button
									type="button"
									className="action-cancel"
									onClick={() => setIsProfileDialogOpen(false)}
									disabled={isSaving}
								>
									Cerrar
								</button>
								<button
									type="submit"
									className="action-save"
									disabled={isSaving}
								>
									{isSaving ? "Guardando..." : "Guardar cambios"}
								</button>
							</div>
						</form>
					</div>
				</div>
			)}

			{/* Nuevo: Modal visual para cambiar contraseña */}
			{isPasswordDialogOpen && (
				<div className="modal-overlay">
					<div className="modal">
						<h3>Cambiar Contraseña</h3>
						<form onSubmit={handleChangePasswordSubmit} className="profile-form">
							<label>
								Contraseña Actual
								<div style={{ position: "relative", display: "flex", alignItems: "center" }}>
									<input
										name="currentPassword"
										value={passwordForm.currentPassword}
										onChange={handlePasswordInputChange}
										type={showCurrentPassword ? "text" : "password"}
										style={{
											width: "100%",
											paddingRight: "44px", // espacio para el icono
											boxSizing: "border-box",
										}}
									/>
									<button
										type="button"
										onClick={() => setShowCurrentPassword((v) => !v)}
										aria-label={showCurrentPassword ? "Ocultar contraseña actual" : "Mostrar contraseña actual"}
										style={{
											position: "absolute",
											right: 8,
											top: "50%",
											transform: "translateY(-50%)",
											border: "none",
											background: "rgba(0,0,0,0.04)",
											padding: 6,
											borderRadius: 6,
											display: "flex",
											alignItems: "center",
											justifyContent: "center",
											cursor: "pointer",
											outline: "none",
											transition: "background 120ms ease, opacity 120ms ease",
											opacity: 0.95,
										}}
										onMouseEnter={(e) => (e.currentTarget.style.background = "rgba(0,0,0,0.06)")}
										onMouseLeave={(e) => (e.currentTarget.style.background = "rgba(0,0,0,0.04)")}
									>
										{showCurrentPassword ? <EyeOff size={18} color="#374151" /> : <Eye size={18} color="#374151" />}
									</button>
								</div>
							</label>

							<label>
								Nueva Contraseña
								<div style={{ position: "relative", display: "flex", alignItems: "center" }}>
									<input
										name="newPassword"
										value={passwordForm.newPassword}
										onChange={handlePasswordInputChange}
										type={showNewPassword ? "text" : "password"}
										style={{
											width: "100%",
											paddingRight: "44px",
											boxSizing: "border-box",
										}}
									/>
									<button
										type="button"
										onClick={() => setShowNewPassword((v) => !v)}
										aria-label={showNewPassword ? "Ocultar nueva contraseña" : "Mostrar nueva contraseña"}
										style={{
											position: "absolute",
											right: 8,
											top: "50%",
											transform: "translateY(-50%)",
											border: "none",
											background: "rgba(0,0,0,0.04)",
											padding: 6,
											borderRadius: 6,
											display: "flex",
											alignItems: "center",
											justifyContent: "center",
											cursor: "pointer",
											outline: "none",
											transition: "background 120ms ease, opacity 120ms ease",
											opacity: 0.95,
										}}
										onMouseEnter={(e) => (e.currentTarget.style.background = "rgba(0,0,0,0.06)")}
										onMouseLeave={(e) => (e.currentTarget.style.background = "rgba(0,0,0,0.04)")}
									>
										{showNewPassword ? <EyeOff size={18} color="#374151" /> : <Eye size={18} color="#374151" />}
									</button>
								</div>
							</label>

							<label>
								Confirmar Nueva Contraseña
								<div style={{ position: "relative", display: "flex", alignItems: "center" }}>
									<input
										name="confirmPassword"
										value={passwordForm.confirmPassword}
										onChange={handlePasswordInputChange}
										type={showConfirmPassword ? "text" : "password"}
										style={{
											width: "100%",
											paddingRight: "44px",
											boxSizing: "border-box",
										}}
									/>
									<button
										type="button"
										onClick={() => setShowConfirmPassword((v) => !v)}
										aria-label={showConfirmPassword ? "Ocultar confirmación de contraseña" : "Mostrar confirmación de contraseña"}
										style={{
											position: "absolute",
											right: 8,
											top: "50%",
											transform: "translateY(-50%)",
											border: "none",
											background: "rgba(0,0,0,0.04)",
											padding: 6,
											borderRadius: 6,
											display: "flex",
											alignItems: "center",
											justifyContent: "center",
											cursor: "pointer",
											outline: "none",
											transition: "background 120ms ease, opacity 120ms ease",
											opacity: 0.95,
										}}
										onMouseEnter={(e) => (e.currentTarget.style.background = "rgba(0,0,0,0.06)")}
										onMouseLeave={(e) => (e.currentTarget.style.background = "rgba(0,0,0,0.04)")}
									>
										{showConfirmPassword ? <EyeOff size={18} color="#374151" /> : <Eye size={18} color="#374151" />}
									</button>
								</div>
							</label>

							{passwordError && (
								<div className="form-error error-message small.error-message">{passwordError}</div>
							)}

							<div className="modal-actions">
								<button
									type="button"
									className="action-cancel"
									onClick={() => setIsPasswordDialogOpen(false)}
									disabled={isChangingPassword}
								>
									Cancelar
								</button>
								<button
									type="submit"
									className="action-save"
									disabled={isChangingPassword}
								>
									{isChangingPassword ? "Aplicando..." : "Cambiar contraseña"}
								</button>
							</div>
						</form>
					
					</div>
				</div>
			)}
		</div>
	);
}
