package org.myprojects.simple_shop_app.auth.model.user;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.myprojects.simple_shop_app.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder
@RequiredArgsConstructor
public class UserDetailsAdapter implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
