package ma.sool.hoguser;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;

@Getter
public class MyUserPrincipal implements UserDetails {

    private final HogUser hogUser;

    public MyUserPrincipal(HogUser hogUser) {
        this.hogUser = hogUser;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(StringUtils.tokenizeToStringArray(hogUser.getRoles(), " "))
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role))
                .toList();
    }

    @Override
    public String getPassword() {
        return hogUser.getPassword();
    }

    @Override
    public String getUsername() {
        return hogUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}