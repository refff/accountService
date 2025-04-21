package account.infrastructure;

import account.domain.Entities.AccountUser;
import account.domain.Entities.Event;
import account.domain.EventAction;
import org.springframework.context.ApplicationEvent;

public class LogEvent extends ApplicationEvent{
    private  Event event;

    public LogEvent(Object source, Event event) {
        super(source);
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
