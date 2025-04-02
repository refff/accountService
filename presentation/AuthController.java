package account.presentation;

import account.domain.AccountUserDTO;
import account.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "api/auth/signup")
    public ResponseEntity<?> createUser(@RequestBody @Valid AccountUserDTO accountUser) {
        return authService.createUser(accountUser);
    }

    @PostMapping(value = "api/auth/changepass")
    public ResponseEntity<?> changePassword(@RequestBody HashMap map) {
        return authService.changePassword(map.get("new_password").toString());
    }

}
