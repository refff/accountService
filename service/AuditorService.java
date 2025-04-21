package account.service;

import account.domain.Entities.Event;
import account.persistance.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditorService {

    private EventRepository eventRepository;

    @Autowired
    public AuditorService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public ResponseEntity<?> getAllEvents() {
        List<Event> allEvents = eventRepository.findAll();
        return new ResponseEntity<>(allEvents, HttpStatus.OK);
    }
}
