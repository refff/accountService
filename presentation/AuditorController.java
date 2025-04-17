package account.presentation;

import account.service.AuditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditorController {

    private AuditorService auditorService;

    @Autowired
    public AuditorController(AuditorService auditorService) {
        this.auditorService = auditorService;
    }

    @GetMapping(value = "api/security/events")
    public ResponseEntity<?> getAllEvents() {
        return auditorService.getAllEvents();
    }

}
