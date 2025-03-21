package account.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AccountUserAdapter implements UserDetails {

    private AccountUser accountUser;

    public AccountUserAdapter(AccountUser accountUser) {
        this.accountUser = accountUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(accountUser.getAuthority()));
    }

    @Override
    public String getPassword() {
        return accountUser.getPassword();
    }

    @Override
    public String getUsername() {
        return accountUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
