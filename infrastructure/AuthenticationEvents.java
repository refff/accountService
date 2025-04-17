package account.infrastructure;

import account.domain.Entities.AccountUser;
import account.domain.Entities.Event;
import account.domain.EventAction;
import account.service.EventService;
import account.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalTime;

@Component
public class AuthenticationEvents {

    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private CreateLogEventPublisher publisher;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        Authentication auth = success.getAuthentication();
        AccountUser user = userService.getUserByEmail(auth.getName());

        if (user.getFailedAttempt() > 0) {
            userService.resetFailAttempt(user);
        }
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failure) {
        Authentication auth = failure.getAuthentication();
        AccountUser user = userService.getUserByEmail(auth.getName());

         if (user != null) {
            if (user.isAccountNonLocked()) {
                if (user.getFailedAttempt() < UserService.MAX_ATTEMPTS) {
                    userService.increaseFailAttempt(user);
                } else {
                    userService.lockUser(user);
                    publisher.publishLogEvent(user, EventAction.BRUTE_FORCE);
                }
            }
        }

        publisher.publishLogEvent(user, EventAction.LOGIN_FAILED);
    }
}
