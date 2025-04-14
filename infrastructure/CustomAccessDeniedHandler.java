package account.infrastructure;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;


public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    public static final Logger LOG = Logger.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException exc) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(
                "{\"status\":403," +
                        "\"error\":\"Forbidden\"," +
                        "\"message\":\"Access Denied!\"," +
                        "\"path\":\"" + request.getRequestURI() + "\"}"
        );
    }
}