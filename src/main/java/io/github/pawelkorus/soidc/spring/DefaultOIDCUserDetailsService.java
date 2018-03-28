package io.github.pawelkorus.soidc.spring;

import io.github.pawelkorus.soidc.IdTokenPayload;
import io.github.pawelkorus.soidc.PublicClaims;

public class DefaultOIDCUserDetailsService implements OIDCUserDetailsService {
    @Override
    public OIDCUserDetails loadUser(IdTokenPayload idTokenPayload) {
        return OIDCUserDetails.builder()
                .sub(idTokenPayload.getAsString(PublicClaims.sub.name()))
                .build();
    }
}
