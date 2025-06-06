package account.service;

import account.domain.Entities.AccountUser;
import account.domain.AccountUserDTO;
import account.domain.Entities.Group;
import account.domain.EventAction;
import account.infrastructure.CreateLogEventPublisher;
import account.infrastructure.CustomExceptions.BreachedPasswordException;
import account.infrastructure.CustomExceptions.ShortPasswordException;
import account.infrastructure.CustomExceptions.UsedPasswordException;
import account.infrastructure.CustomExceptions.UserExistException;
import account.persistance.GroupRepository;
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
    private final GroupRepository groupRepository;
    private final List<String> breachPassword = List.of(
            "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
    @Autowired
    private CreateLogEventPublisher publisher;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.groupRepository = groupRepository;
    }

    public ResponseEntity<?> register(AccountUserDTO request) {
        userRepository.findUserByEmail(request.getEmail())
                .ifPresentOrElse(
                        user -> {throw new UserExistException();},
                        () -> checkBreachedPassword(request.getPassword())
                                .or(() -> checkPasswordLength(request.getPassword())));

        AccountUser user = createUser(request);
        userRepository.save(user);

        AccountUserDTO response = updateDTO(request, user);

        publisher.publishLogEvent(user, EventAction.CREATE_USER, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private AccountUser createUser(AccountUserDTO request) {
        AccountUser accountUser = new AccountUser(
                request.getName(),
                request.getLastName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()));

        assignRoleGroup(accountUser);

        return accountUser;
    }

    private void assignRoleGroup(AccountUser user) {
        Group group;

        if (userRepository.findAll().size() == 0) {
            group = groupRepository.findByCode("ROLE_ADMINISTRATOR");
            user.setAuthorities("ADMIN");
        } else {
            group = groupRepository.findByCode("ROLE_USER");
            user.setAuthorities("USER");
        }

        user.setUserGroup(group);
    }

    private AccountUserDTO updateDTO(AccountUserDTO request, AccountUser accountUser) {
        request.setId(accountUser.getUserId());
        request.setRoles(accountUser.getUserGroup());

        return request;
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
        publisher.publishLogEvent(user, EventAction.CHANGE_PASSWORD, "");

        return Optional.of(new ResponseEntity<>(Map.of(
                "email", user.getEmail(),
                "status","The password has been updated successfully"),
                HttpStatus.OK));
    }
}
