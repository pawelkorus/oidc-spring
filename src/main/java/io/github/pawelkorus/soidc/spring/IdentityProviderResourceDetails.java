package io.github.pawelkorus.soidc.spring;

import io.github.pawelkorus.soidc.ClientConfig;
import io.github.pawelkorus.soidc.IdentityProviderConfig;
import io.github.pawelkorus.soidc.Scope;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import javax.validation.constraints.NotNull;
import java.util.Collections;

public class IdentityProviderResourceDetails extends AuthorizationCodeResourceDetails {

    public IdentityProviderResourceDetails(@NotNull IdentityProviderConfig identityProviderConfig,
                                           @NotNull ClientConfig clientConfig) {
        this.setId(identityProviderConfig.getIssuer());
        this.setClientId(clientConfig.getClientId());
        this.setClientSecret(clientConfig.getClientSecret());
        this.setAccessTokenUri(identityProviderConfig.getTokenEndpoint());
        this.setUserAuthorizationUri(identityProviderConfig.getAuthorizationEndpoint());
        this.setScope(Collections.singletonList(Scope.openid.name()));
        this.setPreEstablishedRedirectUri(clientConfig.getRedirectUri());
        this.setUseCurrentUri(false);
    }
}
