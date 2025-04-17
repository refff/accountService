package account.infrastructure;

import account.domain.Entities.AccountUser;
import account.domain.Entities.Event;
import account.domain.EventAction;
import account.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalTime;

@Component
public class LogEventListener {
    private EventService eventService;

    @Autowired
    public LogEventListener(EventService eventService) {
        this.eventService = eventService;
    }

    @EventListener
    public void logEvent(LogEvent event) {
        eventService.saveEvent(event.getEvent());
    }
}
