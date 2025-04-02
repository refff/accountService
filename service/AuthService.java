package account.service;

import account.domain.AccountUser;
import account.domain.AccountUserDTO;
import account.infrastructure.CustomExceptions.BreachedPasswordException;
import account.infrastructure.CustomExceptions.ShortPasswordException;
import account.infrastructure.CustomExceptions.UsedPasswordException;
import account.infrastructure.CustomExceptions.UserExistException;
import account.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final List<String> breachPassword = List.of(
            "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> createUser(AccountUserDTO request) {
        userRepository.findUserByEmail(request.getEmail())
                .ifPresentOrElse(user -> {throw new UserExistException();},
                        () -> checkBreachedPassword(request.getPassword()));

        AccountUser accountUser = new AccountUser(
                request.getName(),
                request.getLastName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()));

        userRepository.save(accountUser);
        request.setId(accountUser.getUserId());

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    public ResponseEntity<?> getUser(String email) {
        AccountUser user = userRepository.findUserByEmail(email).get();
        return new ResponseEntity<>(AccountUser.convertToDTO(user), HttpStatus.OK);
    }

    public ResponseEntity<?> changePassword(String new_password) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findUserByEmail(auth.getName())
                .map(user -> checkPasswordChange(new_password, user.getPassword())
                        .or(() -> checkPasswordLength(new_password))
                        .or(() -> checkBreachedPassword(new_password))
                        .orElseGet(() -> updatePassword(user, new_password).get()))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    private Optional<ResponseEntity<?>> checkPasswordLength(String password) {
        if (password.length() < 12) {
            throw new ShortPasswordException();
        }
        return Optional.empty();
    }

    private Optional<ResponseEntity<?>> checkBreachedPassword(String password) {
        if(breachPassword.contains(password)) {
            throw new BreachedPasswordException();
        }
        return Optional.empty();
    }

    private Optional<ResponseEntity<?>> checkPasswordChange(String password, String oldPassword) {
        if (passwordEncoder.matches(password, oldPassword)) {
            throw new UsedPasswordException();
        }
        return Optional.empty();
    }

    private Optional<ResponseEntity<?>> updatePassword(AccountUser user, String password) {
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.save(user);

        return Optional.of(new ResponseEntity<>(Map.of(
                "email", user.getEmail(),
                "status","The password has been updated successfully"),
                HttpStatus.OK));
    }
}
