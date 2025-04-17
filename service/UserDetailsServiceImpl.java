package account.service;

import account.domain.Entities.AccountUser;
import account.domain.Entities.Group;
import account.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
        AccountUser customer = userRepository
                .findUserByEmail(email.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Not found!"));

        UserDetails user = User.withUsername(customer.getEmail())
                .password(customer.getPassword())
                .authorities(getAuthorities(customer.getUserGroup()))
                .accountLocked(!customer.isAccountNonLocked()).build();

        return user;
    }

    private Collection<GrantedAuthority> getAuthorities(Set<Group> roles) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getCode())));
        
        return authorities;
    }
}
