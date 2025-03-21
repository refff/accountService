package account.presentation;

import account.domain.AccountUserDTO;
import account.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "api/auth/signup")
    public ResponseEntity<?> createUser(@RequestBody @Valid AccountUserDTO accountUser) {
        return userService.createUser(accountUser);
    }

    @GetMapping(value = "api/empl/payment")
    public ResponseEntity<?> getUserData(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUser(userDetails.getUsername());
    }

    @PostMapping(value = "api/auth/changepass")
    public ResponseEntity<?> changePassword(@RequestBody HashMap map) {
        return userService.changePassword(map.get("new_password").toString());
    }

}
