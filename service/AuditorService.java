package account.service;

import account.persistance.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuditorService {

    private EventRepository eventRepository;

    @Autowired
    public AuditorService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public ResponseEntity<?> getAllEvents() {
        return null;
    }
}
