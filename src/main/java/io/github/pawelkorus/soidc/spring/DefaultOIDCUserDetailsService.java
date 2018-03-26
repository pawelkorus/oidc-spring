package io.github.pawelkorus.soidc.spring;

import io.github.pawelkorus.soidc.IdTokenPayload;
import org.springframework.security.core.userdetails.UserDetails;

public class DefaultOIDCUserDetailsService implements OIDCUserDetailsService {
    @Override
    public UserDetails loadUser(IdTokenPayload idTokenPayload) {
        return OIDCUserDetails.builder(idTokenPayload).build();
    }
}
