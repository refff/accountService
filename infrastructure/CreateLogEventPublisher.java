package account.infrastructure;

import account.domain.Entities.AccountUser;
import account.domain.Entities.Event;
import account.domain.EventAction;
import account.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Optional;

import static account.domain.EventAction.getSubject;

@Component
public class CreateLogEventPublisher {

    private ApplicationEventPublisher applicationEventPublisher;
    private UserService userService;

    @Autowired
    public CreateLogEventPublisher(ApplicationEventPublisher applicationEventPublisher,
                                   UserService userService) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.userService = userService;
    }

    public void publishLogEvent(final AccountUser user, final EventAction action, String info) {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String path = request.getRequestURI().toString();

        HashMap<String, String> data = objectCompile(action, user.getEmail(), path, info);

        Event event = new Event.Builder()
                .setAction(action.name())
                .setDate(LocalTime.now().toString())
                .setObject(data.get("object"))
                .setPath(path)
                .setSubject(data.get("subject")).build();

        LogEvent createLogEvent = new LogEvent(this, event);
        applicationEventPublisher.publishEvent(createLogEvent);
    }

    private HashMap<String, String> objectCompile(EventAction action, String email, String path, String role) {
        String object = "";
        String adminEmail = userService.getAdminEmail();
        String subject = EventAction.getSubject(action).equals("admin") ?  adminEmail: email;

        HashMap<String, String> objectSubject = new HashMap<>();

        switch (action) {
            case CREATE_USER -> {
                object = email;
                subject = "Anonymous";
            }
            case LOCK_USER -> object = "Lock user " + email; //admin
            case UNLOCK_USER -> object = "Unlock user " + email; //admin
            case GRANT_ROLE -> object = "Grant role " + role.substring(5) + " to " + email; //admin
            case REMOVE_ROLE -> object = "Remove role " + role.substring(5) + " from " + email; //admin
            case DELETE_USER -> object = email; //admin
            case LOGIN_FAILED -> object = path; //user
            case BRUTE_FORCE -> object = path; //user
            case ACCESS_DENIED -> object = path; //user
            case CHANGE_PASSWORD -> object = email; //user
        }


        objectSubject.put("object", object);
        objectSubject.put("subject", subject);

        return objectSubject;
    }

}
