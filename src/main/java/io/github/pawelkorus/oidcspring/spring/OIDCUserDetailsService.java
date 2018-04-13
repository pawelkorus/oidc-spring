package io.github.pawelkorus.oidcspring.spring;

import io.github.pawelkorus.oidcspring.IdTokenPayload;
import org.springframework.security.core.userdetails.UserDetails;

public interface OIDCUserDetailsService {

    UserDetails loadUser(IdTokenPayload idTokenPayload);

}
