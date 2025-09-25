    package com.imperialnet.foodstore.users.infrastructure.web.in;

    import jakarta.servlet.http.HttpServletRequest;
    import lombok.extern.slf4j.Slf4j;
    import org.slf4j.MDC;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.GetMapping;

    @Slf4j
    @Controller
    public class AuthController {

        @GetMapping("/login")
        public String loginPage(HttpServletRequest request) {
            String clientIp = request.getRemoteAddr();
            MDC.put("action", "LOGIN_PAGE");
            log.debug("Renderizando pagina de login para IP: {}", clientIp);
            MDC.remove("action");
            return "login";
        }
    }
