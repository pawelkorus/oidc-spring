package io.github.pawelkorus.soidc.spring;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public class VerifiedOIDCTokenAuthentication extends OIDCTokenAuthentication {

    private final UserDetails userDetails;

    public VerifiedOIDCTokenAuthentication(@NotNull String issuer,
                                           @NotNull String token,
                                           @NotNull UserDetails userDetails) {
        super(issuer, token);
        this.userDetails = userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getDetails() {
        return this.userDetails;
    }

    @Override
    public Object getPrincipal() {
        return this.userDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public String getName() {
        return this.userDetails.getUsername();
    }
}
