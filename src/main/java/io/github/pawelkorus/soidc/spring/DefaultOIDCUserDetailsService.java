package io.github.pawelkorus.soidc.spring;

import io.github.pawelkorus.soidc.CommonClaim;
import io.github.pawelkorus.soidc.IdTokenPayload;

public class DefaultOIDCUserDetailsService implements OIDCUserDetailsService {
    @Override
    public OIDCUserDetails loadUser(IdTokenPayload idTokenPayload) {
        return OIDCUserDetails.builder()
                .sub(idTokenPayload.getAsString(CommonClaim.sub.name()))
                .build();
    }
}
