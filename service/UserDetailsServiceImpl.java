package account.service;

import account.domain.AccountUser;
import account.domain.AccountUserAdapter;
import account.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AccountUser accountUser = userRepository
                .findUserByEmail(email.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Not found!"));

        return new AccountUserAdapter(accountUser);
    }
}
