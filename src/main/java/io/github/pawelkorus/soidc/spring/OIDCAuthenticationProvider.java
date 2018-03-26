package io.github.pawelkorus.soidc.spring;

import io.github.pawelkorus.soidc.IdTokenPayload;
import io.github.pawelkorus.soidc.IdentityProviderConfig;
import io.github.pawelkorus.soidc.process.TokenProcessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;

public class OIDCAuthenticationProvider implements AuthenticationProvider {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OIDCAuthenticationProvider.class);
    private final IdentityProviderConfig identityProviderData;
    private final OIDCUserDetailsService oidcUserDetailsService;
    private final TokenProcessor tokenProcessor;

    public OIDCAuthenticationProvider(@NotNull IdentityProviderConfig identityProviderData,
                                      @NotNull OIDCUserDetailsService oidcUserDetailsService,
                                      @NotNull TokenProcessor tokenProcessor) {
        this.identityProviderData = identityProviderData;
        this.oidcUserDetailsService = oidcUserDetailsService;
        this.tokenProcessor = tokenProcessor;
    }

    @Override
    public OIDCTokenAuthentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) return null;

        OIDCTokenAuthentication idTokenAuthentication = (OIDCTokenAuthentication) authentication;

        if (idTokenAuthentication.getIssuer() == null || idTokenAuthentication.getIssuer().compareTo(identityProviderData.getIssuer()) != 0) {
            return null;
        }

        String token = idTokenAuthentication.getIdToken();

        try {
            return authenticate(idTokenAuthentication.getIssuer(), token);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OIDCTokenAuthentication.class.isAssignableFrom(authentication);
    }

    private VerifiedOIDCTokenAuthentication authenticate(String issuer, String idToken) throws Exception {
        IdTokenPayload tokenPayload = tokenProcessor.verifyAndDecode(idToken);
        UserDetails oidcUserDetails = userDetails(tokenPayload);
        return new VerifiedOIDCTokenAuthentication(issuer, idToken, oidcUserDetails);
    }

    private UserDetails userDetails(IdTokenPayload tokenPayload) throws Exception {
        return oidcUserDetailsService.loadUser(tokenPayload);
    }
}
