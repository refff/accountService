package account.infrastructure;

import account.domain.Entities.AccountUser;
import account.domain.Entities.Group;
import account.domain.EventAction;
import account.service.AdminService;
import account.service.EventService;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class AuthenticationEventsListener {

    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private CreateLogEventPublisher publisher;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        Authentication auth = success.getAuthentication();
        AccountUser user = userService.getUserByEmail(auth.getName()).get();

        if (user.getFailedAttempt() > 0) {
            userService.resetFailAttempt(user);
        }
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failure){
        Authentication auth = failure.getAuthentication();

        Optional<AccountUser> userOptional = userService.getUserByEmail(auth.getName());
        AccountUser user = new AccountUser();

        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (user.isAccountNonLocked()) {
                if (!user.getAuthority().equals("ADMIN")) {
                    userService.increaseFailAttempt(user);
                }
                if (user.getFailedAttempt() >= UserService.MAX_ATTEMPTS) {
                    publisher.publishLogEvent(user, EventAction.LOGIN_FAILED, "");
                    publisher.publishLogEvent(user, EventAction.BRUTE_FORCE, "");
                    publisher.publishLogEvent(user, EventAction.LOCK_USER, "");
                    userService.lockUser(user);
                    return;
                }
            } else {
                return;
            }
        } else {
            user.setEmail(failure.getAuthentication().getName());
        }

        publisher.publishLogEvent(user, EventAction.LOGIN_FAILED, "");
    }
}
