package account.presentation;

import account.domain.AccountUserDTO;
import account.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.midi.Soundbank;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "api/auth/signup")
    public ResponseEntity<?> createUser(@RequestBody @Valid AccountUserDTO accountUser) {
        return authService.register(accountUser);
    }

    @PostMapping(value = "api/auth/changepass")
    public ResponseEntity<?> changePassword(@RequestBody HashMap map) {
        return authService.changePassword(map.get("new_password").toString());
    }

    @GetMapping(value = "api/hello")
    public ResponseEntity<?> sayHello() {
        return new ResponseEntity<>(Map.of("message", "hello buddy"), HttpStatus.OK);
    }

    @GetMapping(value = "api/hello/user")
    public ResponseEntity<?> sayHelloUser() {
        return new ResponseEntity<>(Map.of("message", "hello user buddy"), HttpStatus.OK);
    }

}
