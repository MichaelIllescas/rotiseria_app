document.addEventListener('DOMContentLoaded', () => {
    const showLoginBtn = document.getElementById("showLogin");
    const loginForm = document.getElementById("loginForm");
    const usernameInput = document.getElementById("username"); // username tipo email
    const passwordInput = document.getElementById("password");
    const passwordToggle = document.getElementById("passwordToggle");
    const errorDiv = document.getElementById("loginError");

    // Mostrar formulario cuando hacen clic en "Iniciar sesión"
    if (showLoginBtn) {
        showLoginBtn.addEventListener("click", () => {
            loginForm.classList.remove("hidden");
            loginForm.classList.add("fade-in");
        });
    }

    // Toggle de contraseña
    if (passwordToggle) {
        passwordToggle.addEventListener("click", () => {
            const type = passwordInput.type === "password" ? "text" : "password";
            passwordInput.type = type;
            passwordToggle.classList.toggle("toggle-visible", type === "text");
        });
    }

    // Validación al enviar el formulario
    loginForm.addEventListener("submit", (e) => {
        let valid = true;
        let messages = [];

        // Validar email
        const emailValue = usernameInput.value.trim();
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailValue) {
            valid = false;
            messages.push("El email es obligatorio.");
        } else if (!emailRegex.test(emailValue)) {
            valid = false;
            messages.push("El formato del email no es válido.");
        }

        // Validar password
        const passwordValue = passwordInput.value.trim();
        if (!passwordValue) {
            valid = false;
            messages.push("La contraseña es obligatoria.");
        } else if (passwordValue.length < 6) {
            valid = false;
            messages.push("La contraseña debe tener al menos 6 caracteres.");
        }

        // Si hay errores, no enviar y mostrar mensaje
        if (!valid) {
            e.preventDefault();
            if (errorDiv) {
                errorDiv.textContent = messages.join(" ");
                errorDiv.classList.add("show");
            }
        }
    });

   const params = new URLSearchParams(window.location.search);

   // Si viene error del backend
   if (params.has("error")) {
       if (errorDiv) {
           errorDiv.textContent = "Credenciales inválidas, intenta nuevamente.";
           errorDiv.classList.add("show");
       }
       // Mostrar el formulario automáticamente
       loginForm.classList.remove("hidden");
       loginForm.classList.add("fade-in");
   }

   // Si viene logout
   if (params.has("logout")) {
       if (errorDiv) {
           errorDiv.textContent = "Sesión cerrada correctamente.";
           errorDiv.classList.add("show");
       }
       loginForm.classList.remove("hidden");
       loginForm.classList.add("fade-in");
   }

});
