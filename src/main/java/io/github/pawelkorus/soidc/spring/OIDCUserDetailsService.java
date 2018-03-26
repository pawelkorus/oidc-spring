package io.github.pawelkorus.soidc.spring;

import io.github.pawelkorus.soidc.IdTokenPayload;
import org.springframework.security.core.userdetails.UserDetails;

public interface OIDCUserDetailsService {

    UserDetails loadUser(IdTokenPayload idTokenPayload);

}
