package account.infrastructure;

import account.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Component;

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
