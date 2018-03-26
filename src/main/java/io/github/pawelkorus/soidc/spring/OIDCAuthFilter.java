package io.github.pawelkorus.soidc.spring;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class OIDCAuthFilter extends AbstractAuthenticationProcessingFilter {

    private OAuth2RestOperations restTemplate;

    public OIDCAuthFilter(RequestMatcher requestMatcher, OAuth2RestOperations restTemplate) {
        super(requestMatcher);
        this.restTemplate = restTemplate;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        OAuth2AccessToken accessToken;
        try {
            accessToken = restTemplate.getAccessToken();
        } catch (OAuth2Exception e) {
            throw new AuthenticationServiceException("Could not obtain access token", e);
        }

        if (accessToken == null) {
            throw new AuthenticationServiceException("Cound not obrain access token. Got null access token");
        }

        String idToken = Optional.ofNullable(accessToken.getAdditionalInformation()).map(info -> info.get("id_token")).map(Object::toString)
            .orElseThrow(() -> new AuthenticationServiceException("Identity provider didn't send id token"));

        return new OIDCTokenAuthentication(restTemplate.getResource().getId(), idToken);
    }
}
