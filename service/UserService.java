package account.service;

import account.domain.Entities.AccountUser;
import account.infrastructure.CustomExceptions.CustomNotFoundException;
import account.infrastructure.CustomExceptions.UserNotExistException;
import account.persistance.UserRepository;
import org.h2.security.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    public static final int MAX_ATTEMPTS = 5;

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<AccountUser> getUserByEmail(String email) {


        return userRepository.findUserByEmail(email);
    }

    public String getAdminEmail() {
        AccountUser admin = userRepository.getById(1);
        return admin.getEmail();
    }

    public void increaseFailAttempt(AccountUser user) {
        int newFailAttempt = user.getFailedAttempt() + 1;
        user.setFailedAttempt(newFailAttempt);
        userRepository.updateFailAttempts(newFailAttempt, user.getEmail());
    }

    public void resetFailAttempt(AccountUser user) {
        user.setFailedAttempt(0);
        userRepository.updateFailAttempts(0, user.getEmail());
    }

    public void lockUser(AccountUser user) {
        user.setAccountNonLocked(false);
        userRepository.save(user);
    }

}
