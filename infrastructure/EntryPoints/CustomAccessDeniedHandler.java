package account.infrastructure.EntryPoints;


import account.domain.Entities.AccountUser;
import account.domain.EventAction;
import account.infrastructure.CreateLogEventPublisher;
import account.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;


public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private CreateLogEventPublisher publisher;
    @Autowired
    private UserService userService;

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountUser user = userService.getUserByEmail(authentication.getName()).get();
        publisher.publishLogEvent(user, EventAction.ACCESS_DENIED, "");
    }
}