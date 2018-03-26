package io.github.pawelkorus.soidc.spring;

import io.github.pawelkorus.soidc.IdTokenPayload;
import io.github.pawelkorus.soidc.PublicClaims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class OIDCUserDetails implements UserDetails {

    private String userId;

    public OIDCUserDetails(String sub) {
        this.userId = sub;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.userId;
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

    public static OIDCUserDetailsBuilder builder(IdTokenPayload idTokenPayload) {
        return new OIDCUserDetailsBuilder(idTokenPayload);
    }

    public static class OIDCUserDetailsBuilder {
        private IdTokenPayload idTokenPayload;

        public OIDCUserDetailsBuilder(IdTokenPayload idTokenPayload) {
            this.idTokenPayload = idTokenPayload;
        }

        public OIDCUserDetails build() {
            return new OIDCUserDetails(idTokenPayload.getAsString(PublicClaims.sub.name()));
        }
    }
}
