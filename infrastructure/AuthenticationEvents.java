package account.infrastructure;

import account.domain.AccountUser;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents {

    @Autowired
    private UserService userService;

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
            if(user.isAccountNonLocked()) {
                if (user.getFailedAttempt() < UserService.MAX_ATTEMPTS) {
                    userService.increaseFailAttempt(user);
                } else {
                    userService.lockUser(user);
                }
            }
        }
    }
}
