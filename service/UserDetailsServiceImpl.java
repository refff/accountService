package account.service;

import account.domain.AccountUser;
import account.domain.AccountUserAdapter;
import account.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Service
@Transactional
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

        accountUser.setAuthorities(accountUser.getAuthority());

        return new AccountUserAdapter(accountUser);
    }

    private Collection<GrantedAuthority> getAuthorities(Set<String> roles) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        
        return authorities;
    }
}
